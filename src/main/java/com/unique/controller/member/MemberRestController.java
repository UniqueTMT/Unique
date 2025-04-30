package com.unique.controller.member;

import com.unique.dto.member.*;
import com.unique.entity.member.MemberEntity;
import com.unique.impl.member.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;



@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberRestController {
    private final MemberServiceImpl memberService;

    /**
     * 로그인 후 인가처리 메소드
     * @param authentication
     * @return 권한에 맞게 페이지 이동
     */
    @GetMapping("/route")
    public ResponseEntity<Map<String, Object>> ctlRouteByRole(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401)
                .body(Map.of("dmRoute",
                    Map.of("message", "로그인 후 이용 가능합니다.", "roles", "[]")));
        }

        String userIdStr = authentication.getName(); // 로그인한 사용자 학번 (String)
        Long userId;
        try {
            userId = Long.parseLong(userIdStr); // 🔁 Long 변환
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("dmRoute", Map.of("message", "잘못된 사용자 ID입니다.")));
        }

        List<String> roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        // 🔍 Member 조회 → userSeq 얻기
        Optional<MemberEntity> memberOpt = memberService.svcFindByUserid(userId);
        if (memberOpt.isEmpty()) {
            return ResponseEntity.status(404)
                .body(Map.of("dmRoute", Map.of("message", "해당 사용자를 찾을 수 없습니다.")));
        }

        MemberEntity member = memberOpt.get();
        Long userSeq = member.getUserSeq();

        String message;
        if (roles.contains("ROLE_ADMIN")) {
            message = "관리자 페이지로 이동";
        } else if (roles.contains("ROLE_PROFESSOR") || roles.contains("ROLE_STUDENT")) {
            message = "사용자 페이지로 이동";
        } else {
            return ResponseEntity.status(403).body(Map.of("dmRoute", Map.of(
                "userId", userId,
                "userSeq", userSeq,
                "roles", roles,
                "message", "권한이 없습니다."
            )));
        }

        // 최종 응답 payload에 userSeq 포함
        Map<String, Object> dmRoutePayload = Map.of(
            "userId", userId,
            "userSeq", userSeq,     // ← 여기에 포함!
            "roles", roles,
            "message", message
        );

        return ResponseEntity.ok(Map.of("dmRoute", dmRoutePayload));
    }


    /**
     * [POST] /api/member/find-id
     * - 설명: 사용자 이름과 이메일을 입력받아 해당하는 사용자의 userId(학번)를 마스킹된 형태로 반환
     * - 예: 사용자 userId = "20140293" → "20****93"
     *
     * @param request FindIdRequestDTO 객체 (username, email)
     * @return ResponseEntity<String> 마스킹된 userId 또는 오류 메시지
     */
    @PostMapping("/find-id")
    public ResponseEntity<Map<String,Object>> ctlFindUserId(@RequestBody FindIdRequestWrapper wrapper){

        // 래퍼에서 실제 DTO 꺼내기
        FindIdRequestDTO req = wrapper.getData().getDmFindId();
        String username = req.getUsername();
        String email    = req.getEmail();

        System.out.println("username" + username);
        System.out.println("email" + email);

        // null 체크
        if (username == null || email == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("dmFindId",
                    Map.of("message","이름과 이메일을 모두 입력하세요.")));
        }

        // 서비스 호출
        String mask = memberService.svcFindUserIdByInfo(username, email);

        Map<String,String> payload = new HashMap<>();
        if (mask != null) {
            payload.put("userId",   mask);
            payload.put("message", "아이디 조회 성공");
        } else {
            payload.put("message","일치하는 정보가 없습니다.");
        }

        return ResponseEntity.ok(Map.of("dmFindId", payload));
    }

//    @PostMapping("/find-id")
//    public ResponseEntity<String> ctlFindUserId(@RequestBody FindIdRequestDTO request) {
//        String maskUserId = memberService.svcFindUserIdByInfo(request.getUsername(), request.getEmail());
//
//        if (maskUserId == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("일치하는 정보가 없습니다.");
//        }
//
//        return ResponseEntity.ok("회원님의 아이디는 " + maskUserId + " 입니다.");
//    }

    /**
     * [POST] /api/member/find-password
     * <p>
     * 설명 : 학번(userId), 이름(username), 이메일(email)을 입력받아 해당 회원 정보가 일치할 경우
     *         무작위 임시 비밀번호를 생성하여 회원 이메일로 전송하고, DB의 비밀번호를 임시 비밀번호로 업데이트한다.
     *         성공 시 "임시 비밀번호가 이메일로 전송되었습니다." 메시지 반환.
     * </p>
     *
     * @param request FindPwRequestDTO 객체 (userId, username, email)
     * @return ResponseEntity<String> 결과 메시지
     */
    @PostMapping("/find-password")
    public ResponseEntity<Map<String,Object>> ctlFindPassword(
        @RequestBody FindPwRequestWrapper wrapper) {

        // (1) 래퍼에서 실제 DTO 꺼내기
        FindPwRequestDTO req = wrapper.getData().getDmFindPw();
        Long   userId   = req.getUserId();
        String username = req.getUsername();
        String email    = req.getEmail();

        // (2) 필수값 체크
        if (userId == null || username == null || email == null) {
            return ResponseEntity
                .badRequest()
                // ★ ★ 최상위 키를 dmFindPw 로!
                .body(Map.of("dmFindPw",
                    Map.of("message", "학번·이름·이메일을 모두 입력하세요.")
                ));
        }

        try {
            // (3) 서비스 호출
            boolean success = memberService.svcFindAndResetPassword(userId, username, email);

            // (4) payload 구성 — 무조건 "message" 하나만
            Map<String,String> payload = new HashMap<>();
            payload.put("message",
                success
                    ? "임시 비밀번호가 이메일로 전송되었습니다."
                    : "임시 비밀번호 재설정에 실패했습니다."
            );
            System.out.println("payload : " + payload);
            System.out.println("Map.of(\"dmFindPw\", payload) : " + Map.of("dmFindPw", payload));

            // (5) 최종 JSON: { "dmFindPw": { "message": "..." } }
            return ResponseEntity.ok(Map.of("dmFindPw", payload));

        } catch (RuntimeException ex) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("dmFindPw",
                    Map.of("message", ex.getMessage())
                ));
        }
    }


//    @GetMapping("/member")
//    public ResponseEntity<List<MemberEntity>> ctlMemberList() {
//        return ResponseEntity.ok(memberService.svcMemberList());
//    }
//
//    @GetMapping("/member/{id}")
//    public ResponseEntity<Optional<MemberEntity>> ctlMemberDetail(@PathVariable Long id) {
//        return ResponseEntity.ok(memberService.svcMemberDetail(id));
//    }
//
//    @PostMapping("/member")
//    public void ctlMemberInsert(@RequestBody MemberEntity entity) {
//        memberService.svcMemberInsert(entity);
//    }
//
//    @PutMapping("/member")
//    public void ctlMemberUpdate(@RequestBody MemberEntity entity) {
//        memberService.svcMemberUpdate(entity);
//    }
//
//    @DeleteMapping("/member/{id}")
//    public void ctlMemberDelete(@PathVariable(value="id") Long id) {
//        memberService.svcMemberDelete(id);
//    }



//    @GetMapping("/member-info/{userSeq}")
//    public ResponseEntity<Optional<MemberInfoDTO>> ctlMemberInfo(@PathVariable(value = "userSeq") Long userSeq) {
//        return ResponseEntity.ok(memberServiceImpl.svcGetMemberInfo(userSeq));
//    }



    // 유저 정보 불러오기 (마이페이지) - 경준
    @GetMapping("/member-info/{userSeq}")
    public ResponseEntity<Optional<MemberInfoDTO>> ctlMemberInfo(@PathVariable(value = "userSeq") Long userSeq) {
        return ResponseEntity.ok(memberService.svcGetMemberInfo(userSeq));
    }


    // 유저 비밀번호 변경 - 경준
    @PutMapping("/change-password/{userSeq}")
    public ResponseEntity<String> ctlChangePassword(
        @PathVariable(value = "userSeq") Long userSeq
        ,@RequestParam(value = "oldPassword") String oldPassword
        ,@RequestParam(value = "newPassword") String newPassword
    ) {
        try {
            boolean success = memberService.svcChangePassword(userSeq,oldPassword,newPassword);
            if (success) {
                return ResponseEntity.ok("비밀번호 변경완료");
            }else {
                return ResponseEntity.badRequest().body("비밀번호 변경실패");
            }
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
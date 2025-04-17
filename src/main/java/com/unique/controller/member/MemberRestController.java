package com.unique.controller.member;

import com.unique.dto.member.MemberInfoDTO;
import com.unique.dto.member.FindIdRequestDTO;
import com.unique.dto.member.FindPwRequestDTO;
import com.unique.dto.member.LoginRequestDTO;
import com.unique.entity.member.MemberEntity;
import com.unique.impl.member.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> ctlRouteByRole(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("로그인 후 이용 가능합니다.");
        }

        String userId = authentication.getName(); // 로그인한 사용자 ID
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList(); // 역할 리스트

        String message;

        if (roles.contains("ROLE_ADMIN")) {
            message = "관리자 페이지로 이동";
        } else if (roles.contains("ROLE_PROFESSOR") || roles.contains("ROLE_STUDENT")) {
            message = "사용자 페이지로 이동";
        } else {
            return ResponseEntity.status(403).body(Map.of(
                    "userId", userId,
                    "roles", roles,
                    "message", "권한이 없습니다."
            ).toString());
        }

        return ResponseEntity.ok(Map.of(
                "userId", userId,
                "roles", roles,
                "message", message
        ).toString());
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

//    // [1] 로그인 - 이제무
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {
//        String testId = "20140293";
//        String testPw = "111";
//
//        if (testId.equals(loginRequest.getUserId()) && testPw.equals(loginRequest.getPassword())) {
//            return ResponseEntity.ok("로그인되었습니다.");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("아이디와 비밀번호를 확인하세요");
//        }
//    }
//
//    // [2] 아이디 찾기 - 이제무
//    @PostMapping("/find-id")
//    public ResponseEntity<String> findId(@RequestBody FindIdRequestDTO request) {
//        String testName = "아무개";
//        String testEmail = "dd@dd.com";
//        String testUserId = "20140293";
//
//        if (testName.equals(request.getName()) && testEmail.equals(request.getEmail())) {
//            return ResponseEntity.ok("아이디는 " + testUserId + "입니다.");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("일치하는 정보가 없습니다.");
//        }
//    }
//
//    // [3] 비밀번호 찾기 - 이제무
//    @PostMapping("/find-password")
//    public ResponseEntity<String> findPassword(@RequestBody FindPwRequestDTO request) {
//        String testUserId = "20140293";
//        String testEmail = "dd@dd.com";
//        String tempPw = "111";
//
//        if (testUserId.equals(request.getUserId()) && testEmail.equals(request.getEmail())) {
//            return ResponseEntity.ok("임시 비밀번호는 " + tempPw + "입니다.");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("일치하는 정보가 없습니다.");
//        }
//    }

}
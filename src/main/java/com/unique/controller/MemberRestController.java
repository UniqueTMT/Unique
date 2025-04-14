package com.unique.controller;

import com.unique.dto.MemberInfoDTO;
import com.unique.dto.member.FindIdRequestDTO;
import com.unique.dto.member.FindPwRequestDTO;
import com.unique.dto.member.LoginRequestDTO;
import com.unique.entity.MemberEntity;
import com.unique.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberRestController {
    private final MemberServiceImpl memberService;

    @GetMapping("/member")
    public ResponseEntity<List<MemberEntity>> ctlMemberList() {
        return ResponseEntity.ok(memberService.svcMemberList());
    }

    @GetMapping("/member/{id}")
    public ResponseEntity<Optional<MemberEntity>> ctlMemberDetail(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.svcMemberDetail(id));
    }

    @PostMapping("/member")
    public void ctlMemberInsert(@RequestBody MemberEntity entity) {
        memberService.svcMemberInsert(entity);
    }

    @PutMapping("/member")
    public void ctlMemberUpdate(@RequestBody MemberEntity entity) {
        memberService.svcMemberUpdate(entity);
    }

    @DeleteMapping("/member/{id}")
    public void ctlMemberDelete(@PathVariable(value="id") Long id) {
        memberService.svcMemberDelete(id);
    }




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

    // [1] 로그인 - 이제무
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {
        String testId = "20140293";
        String testPw = "111";

        if (testId.equals(loginRequest.getUserId()) && testPw.equals(loginRequest.getPassword())) {
            return ResponseEntity.ok("로그인되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("아이디와 비밀번호를 확인하세요");
        }
    }

    // [2] 아이디 찾기 - 이제무
    @PostMapping("/find-id")
    public ResponseEntity<String> findId(@RequestBody FindIdRequestDTO request) {
        String testName = "아무개";
        String testEmail = "dd@dd.com";
        String testUserId = "20140293";

        if (testName.equals(request.getName()) && testEmail.equals(request.getEmail())) {
            return ResponseEntity.ok("아이디는 " + testUserId + "입니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("일치하는 정보가 없습니다.");
        }
    }

    // [3] 비밀번호 찾기 - 이제무
    @PostMapping("/find-password")
    public ResponseEntity<String> findPassword(@RequestBody FindPwRequestDTO request) {
        String testUserId = "20140293";
        String testEmail = "dd@dd.com";
        String tempPw = "111";

        if (testUserId.equals(request.getUserId()) && testEmail.equals(request.getEmail())) {
            return ResponseEntity.ok("임시 비밀번호는 " + tempPw + "입니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("일치하는 정보가 없습니다.");
        }
    }
}
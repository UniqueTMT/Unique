package com.unique.controller;

import com.unique.dto.MemberInfoDTO;
import com.unique.entity.MemberEntity;
import com.unique.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



@RestController
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


}
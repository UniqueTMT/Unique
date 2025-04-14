package com.unique.controller.admin;

import com.unique.dto.member.MemberInfoDTO;
import com.unique.entity.member.MemberEntity;
import com.unique.impl.member.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminRestController {
    private final MemberServiceImpl memberService;

    //관리자 기능 관련 컨트롤러 입니다. 추후 사용 예정입니다..

    @GetMapping("/admin")
    public ResponseEntity<List<MemberEntity>> ctlMemberList() {
        return ResponseEntity.ok(memberService.svcMemberList());
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<Optional<MemberEntity>> ctlMemberDetail(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.svcMemberDetail(id));
    }

    @PostMapping("/admin")
    public void ctlMemberInsert(@RequestBody MemberEntity entity) {
        memberService.svcMemberInsert(entity);
    }

    @PutMapping("/admin")
    public void ctlMemberUpdate(@RequestBody MemberEntity entity) {
        memberService.svcMemberUpdate(entity);
    }

    @DeleteMapping("/admin/{id}")
    public void ctlMemberDelete(@PathVariable(value="id") Long id) {
        memberService.svcMemberDelete(id);
    }

    @GetMapping("/admin-info/{userSeq}")
    public ResponseEntity<Optional<MemberInfoDTO>> ctlMemberInfo(@PathVariable(value = "userSeq") Long userSeq) {
        return ResponseEntity.ok(memberService.svcGetMemberInfo(userSeq));
    }
}
package com.unique.controller;

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
    private final MemberServiceImpl memberServiceImpl;

    @GetMapping("/member")
    public ResponseEntity<List<MemberEntity>> ctlMemberList() {
        return ResponseEntity.ok(memberServiceImpl.svcMemberList());
    }

    @GetMapping("/member/{id}")
    public ResponseEntity<Optional<MemberEntity>> ctlMemberDetail(@PathVariable Long id) {
        return ResponseEntity.ok(memberServiceImpl.svcMemberDetail(id));
    }

    @PostMapping("/member")
    public void ctlMemberInsert(@RequestBody MemberEntity entity) {
        memberServiceImpl.svcMemberInsert(entity);
    }

    @PutMapping("/member")
    public void ctlMemberUpdate(@RequestBody MemberEntity entity) {
        memberServiceImpl.svcMemberUpdate(entity);
    }

    @DeleteMapping("/member/{id}")
    public void ctlMemberDelete(@PathVariable(value="id") Long id) {
        memberServiceImpl.svcMemberDelete(id);
    }
}
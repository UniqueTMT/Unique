package com.unique.controller;
import com.unique.entity.MemberLogEntity;
import com.unique.service.MemberLogServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberLogRestController {
    private final MemberLogServiceImpl memberLogServiceImpl;

    @GetMapping("/memberlog")
    public ResponseEntity<List<MemberLogEntity>> ctlMemberLogList() {
        return ResponseEntity.ok(memberLogServiceImpl.svcMemberLogList());
    }

    @GetMapping("/memberlog/{id}")
    public ResponseEntity<Optional<MemberLogEntity>> ctlMemberLogDetail(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(memberLogServiceImpl.svcMemberLogDetail(id));
    }

    @PostMapping("/memberlog")
    public void ctlMemberLogInsert(@RequestBody MemberLogEntity entity) {
        memberLogServiceImpl.svcMemberLogInsert(entity);
    }

    @PutMapping("/memberlog")
    public void ctlMemberLogUpdate(@RequestBody MemberLogEntity entity) {
        memberLogServiceImpl.svcMemberLogUpdate(entity);
    }

    @DeleteMapping("/memberlog/{id}")
    public void ctlMemberLogDelete(@PathVariable(value="id") Long id) {
        memberLogServiceImpl.svcMemberLogDelete(id);
    }
}

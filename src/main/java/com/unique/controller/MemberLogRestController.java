//package com.unique.controller;
//import com.unique.entity.MemberLogEntity;
//import com.unique.service.MemberLogServiceImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequiredArgsConstructor
//public class MemberLogRestController {
//    private final MemberLogServiceImpl memberLogService;
//
//    @GetMapping("/memberlog")
//    public ResponseEntity<List<MemberLogEntity>> ctlMemberLogList() {
//        return ResponseEntity.ok(memberLogService.svcMemberLogList());
//    }
//
//    @GetMapping("/memberlog/{id}")
//    public ResponseEntity<Optional<MemberLogEntity>> ctlMemberLogDetail(@PathVariable(value="id") Long id) {
//        return ResponseEntity.ok(memberLogService.svcMemberLogDetail(id));
//    }
//
//    @PostMapping("/memberlog")
//    public void ctlMemberLogInsert(@RequestBody MemberLogEntity entity) {
//        memberLogService.svcMemberLogInsert(entity);
//    }
//
//    @PutMapping("/memberlog")
//    public void ctlMemberLogUpdate(@RequestBody MemberLogEntity entity) {
//        memberLogService.svcMemberLogUpdate(entity);
//    }
//
//    @DeleteMapping("/memberlog/{id}")
//    public void ctlMemberLogDelete(@PathVariable(value="id") Long id) {
//        memberLogService.svcMemberLogDelete(id);
//    }
//}

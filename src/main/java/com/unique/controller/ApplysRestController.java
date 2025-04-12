package com.unique.controller;

import com.unique.dto.UserExamHistoryDTO;
import com.unique.entity.ApplysEntity;
import com.unique.service.ApplysServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class ApplysRestController {
    private final ApplysServiceImpl applysServiceImpl;

    @GetMapping("/applys")
    public ResponseEntity<List<ApplysEntity>> ctlApplysList() {
        return ResponseEntity.ok(applysServiceImpl.svcApplysList());
    }

    @GetMapping("/applys/{id}")
    public ResponseEntity<Optional<ApplysEntity>> ctlApplysDetail(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(applysServiceImpl.svcApplysDetail(id));
    }

    @PostMapping("/applys")
    public void ctlApplysInsert(@RequestBody ApplysEntity entity) {
        applysServiceImpl.svcApplysInsert(entity);
    }

    @PutMapping("/applys")
    public void ctlApplysUpdate(@RequestBody ApplysEntity entity) {
        applysServiceImpl.svcApplysUpdate(entity);
    }

    @DeleteMapping("/applys/{id}")
    public void ctlApplysDelete(@PathVariable(value="id") Long id) {
        applysServiceImpl.svcApplysDelete(id);
    }

    @GetMapping("/apply-history/{userSeq}")
    public ResponseEntity<List<UserExamHistoryDTO>> ctlFindAllExamHistory(@PathVariable(value = "userSeq") Long userSeq) {
        return ResponseEntity.ok(applysServiceImpl.myFindAllExamHistory(userSeq));
    }
}

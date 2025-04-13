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
    private final ApplysServiceImpl applysService;

    @GetMapping("/applys")
    public ResponseEntity<List<ApplysEntity>> ctlApplysList() {
        return ResponseEntity.ok(applysService.svcApplysList());
    }

    @GetMapping("/applys/{id}")
    public ResponseEntity<Optional<ApplysEntity>> ctlApplysDetail(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(applysService.svcApplysDetail(id));
    }

    @PostMapping("/applys")
    public void ctlApplysInsert(@RequestBody ApplysEntity entity) {
        applysService.svcApplysInsert(entity);
    }

    @PutMapping("/applys")
    public void ctlApplysUpdate(@RequestBody ApplysEntity entity) {
        applysService.svcApplysUpdate(entity);
    }

    @DeleteMapping("/applys/{id}")
    public void ctlApplysDelete(@PathVariable(value="id") Long id) {
        applysService.svcApplysDelete(id);
    }

    @GetMapping("/apply-history/{userSeq}")
    public ResponseEntity<List<UserExamHistoryDTO>> ctlFindAllExamHistory(@PathVariable(value = "userSeq") Long userSeq) {
        return ResponseEntity.ok(applysService.myFindAllExamHistory(userSeq));
    }
}

package com.unique.controller;

import com.unique.dto.UserExamHistoryDTO;
import com.unique.dto.UserExamHistoryDetailDTO;
import com.unique.entity.ApplysEntity;
import com.unique.service.ApplysServiceImpl;
import com.unique.service.UserExamHistoryDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class ApplysRestController {
    private final ApplysServiceImpl applysServiceImpl;
    private final UserExamHistoryDetailServiceImpl userExamHistoryDetailServiceImpl;

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

    //유저 응시 시험 리스트
    @GetMapping("/apply-history/{userSeq}")
    public ResponseEntity<List<UserExamHistoryDTO>> ctlFindAllExamHistory(@PathVariable(value = "userSeq") Long userSeq) {
        return ResponseEntity.ok(applysServiceImpl.myFindAllExamHistory(userSeq));
    }

    //유저 응시 시험 세부 결과
    @GetMapping("/apply-history/{userSeq}/{examSeq}")
    public ResponseEntity<UserExamHistoryDetailDTO> ctlGetExamResult(
            @PathVariable(value = "userSeq") Long userSeq,
            @PathVariable(value = "examSeq") Long examSeq
    ) {
        try {
            UserExamHistoryDetailDTO result = userExamHistoryDetailServiceImpl.svcGetExamResult(userSeq, examSeq);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("Error fetching exam result: " + e.getMessage());
            e.printStackTrace(); // 예외 내용을 출력
            return ResponseEntity.notFound().build();
        }
    }
}

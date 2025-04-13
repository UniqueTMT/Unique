package com.unique.controller;

import com.unique.dto.ExamDTO;
import com.unique.dto.ExamDetailDTO;
import com.unique.dto.TestDTO;
import com.unique.service.ExamServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ExamRestController {

    private final ExamServiceImpl examService;

    @GetMapping()
    public ResponseEntity<List<TestDTO>> ctlFindAll() {
        return ResponseEntity.ok(examService.svcFindAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<TestDTO>> ctlFindById(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(examService.svcFindById(id));
    }

    @PostMapping
    public void ctlInsert(@RequestBody TestDTO dto) {
        examService.svcInsert(dto);
    }

    @PutMapping
    public void ctlUpdate(@RequestBody TestDTO dto) {
        examService.svcUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public void ctlDelete(@PathVariable(value="id") Long id) {
        examService.svcDelete(id);
    }

    //문제은행 카테고리별 시험지 상세 보기
    @GetMapping("/quizbank-detail")
    public ResponseEntity<List<ExamDTO>> ctlFindExamWithQuizList() {
        return ResponseEntity.ok(examService.svcFindExamWithQuizList());
    }


}
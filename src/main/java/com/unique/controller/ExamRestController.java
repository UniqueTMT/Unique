package com.unique.controller;

import com.unique.dto.ExamDTO;
import com.unique.dto.AnswerDetailDTO;
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

    @GetMapping("/examlist")
    public ResponseEntity<List<AnswerDetailDTO>> ctlFindAll() {
        return ResponseEntity.ok(examService.svcFindAll());
    }

    @GetMapping("/{userid}")
    public ResponseEntity<Optional<AnswerDetailDTO>> ctlFindById(@PathVariable(value="userid") Long userid) {
        return ResponseEntity.ok(examService.svcFindById(userid));
    }

    @PostMapping
    public void ctlInsert(@RequestBody AnswerDetailDTO dto) {
        examService.svcInsert(dto);
    }

    @PutMapping
    public void ctlUpdate(@RequestBody AnswerDetailDTO dto) {
        examService.svcUpdate(dto);
    }

    @DeleteMapping("/{userid}")
    public void ctlDelete(@PathVariable(value="id") Long userid) {
        examService.svcDelete(userid);
    }

    //문제은행 카테고리별 시험지 상세 보기
    @GetMapping("/quizbank-detail")
    public ResponseEntity<List<ExamDTO>> ctlFindExamWithQuizList() {
        return ResponseEntity.ok(examService.svcFindExamWithQuizList());
    }


}
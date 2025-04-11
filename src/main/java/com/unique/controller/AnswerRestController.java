package com.unique.controller;

import com.unique.dto.TestDTO;
import com.unique.entity.AnswerEntity;
import com.unique.service.AnswerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


// AnswerRestController.java
@RestController
@RequiredArgsConstructor
public class AnswerRestController {
    private final AnswerServiceImpl answerServiceImpl;

    //------------------------------- 수민 조인 샘플 예시 ---------------------------
//    @GetMapping("/test")
//    public ResponseEntity<List<TestDTO>> ctlExamResult() {
//        List<TestDTO> result = answerServiceImpl.svcTest();
//        return ResponseEntity.ok(result);
//    }
    //----------------------------------------------------------------------------


    @GetMapping("/answers")
    public ResponseEntity<List<AnswerEntity>> ctlGetAllAnswers() {
        return ResponseEntity.ok(answerServiceImpl.svcGetAllAnswers());
    }

    @GetMapping("/answers/{id}")
    public ResponseEntity<Optional<AnswerEntity>> ctlGetAnswer(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(answerServiceImpl.svcGetAnswer(id));
    }

    @PostMapping("/answers")
    public void ctlCreateAnswer(@RequestBody AnswerEntity answer) {
        answerServiceImpl.svcCreateAnswer(answer);
    }

    @PutMapping("/answers")
    public void ctlUpdateAnswer(@RequestBody AnswerEntity answer) {
        answerServiceImpl.svcUpdateAnswer(answer);
    }

    @DeleteMapping("/answers/{id}")
    public void ctlDeleteAnswer(@PathVariable(value="id") Long id) {
        answerServiceImpl.svcDeleteAnswer(id);
    }
}


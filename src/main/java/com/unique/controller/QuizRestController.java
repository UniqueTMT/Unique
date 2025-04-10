package com.unique.controller;
import com.unique.entity.QuizEntity;
import com.unique.service.QuizService;
import com.unique.service.QuizServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class QuizRestController {
    private final QuizService quizService;

    @GetMapping("/quiz")
    public ResponseEntity<List<QuizEntity>> ctlQuizList() {
        return ResponseEntity.ok(quizService.svcQuizList());
    }

    @GetMapping("/quiz/{id}")
    public ResponseEntity<Optional<QuizEntity>> ctlQuizDetail(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(quizService.svcQuizDetail(id));
    }

    @PostMapping("/quiz")
    public void ctlQuizInsert(@RequestBody QuizEntity entity) {
        quizService.svcQuizInsert(entity);
    }

    @PutMapping("/quiz")
    public void ctlQuizUpdate(@RequestBody QuizEntity entity) {
        quizService.svcQuizUpdate(entity);
    }

    @DeleteMapping("/quiz/{id}")
    public void ctlQuizDelete(@PathVariable(value="id") Long id) {
        quizService.svcQuizDelete(id);
    }
}

package com.unique.controller.quiz;
import com.unique.dto.quiz.QuizDTO;
import com.unique.entity.quiz.QuizEntity;
import com.unique.service.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizRestController {
    private final QuizService quizService;

    @PostMapping("/quizlist")
    public void ctlQuizInsert(@RequestBody QuizEntity entity) {
        quizService.svcQuizInsert(entity);
    }

    //특정 시험(examSeq)에 해당하는 예상문제 목록
    @GetMapping("/list/{examSeq}")
    public List<QuizDTO> ctlGetQuizList(@PathVariable Long examSeq) {
        return quizService.svcGetQuizList(examSeq);
    }

    //특정 문제(quizSeq)에 대해 사용자가 내용을 수정한 결과를 저장
    @PutMapping("/{quizSeq}")
    public void ctlUpdateQuiz(@PathVariable Long quizSeq, @RequestBody QuizDTO dto) {
        quizService.svcUpdateQuiz(quizSeq, dto);
    }

    //특정 문제(quizSeq)를 삭제
    @DeleteMapping("/{quizSeq}")
    public void ctlDeleteQuiz(@PathVariable Long quizSeq) {
        quizService.svcDeleteQuiz(quizSeq);
    }

    //특정 시험(examSeq)에 등록된 문제들을 출제 완료 상태로 전환
    @PostMapping("/publish/{examSeq}")
    public void ctlPublishExam(@PathVariable Long examSeq) {
        quizService.svcPublishExam(examSeq);
    }
}

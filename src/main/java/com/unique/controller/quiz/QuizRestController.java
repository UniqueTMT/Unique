package com.unique.controller.quiz;
import com.unique.dto.quiz.QuizDTO;
import com.unique.entity.quiz.QuizEntity;
import com.unique.service.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizRestController {
    private final QuizService quizService;

    @PostMapping("/quizlist")
    public void ctlQuizInsert(@RequestBody QuizEntity entity) {
        quizService.svcQuizInsert(entity);
    }
//
//    //특정 시험(examSeq)에 해당하는 예상문제 목록
//    @GetMapping("/list/{examSeq}")
//    public List<QuizDTO> ctlGetQuizList(@PathVariable Long examSeq) {
//        return quizService.svcGetQuizList(examSeq);
//    }
//
//    //특정 문제(quizSeq)에 대해 사용자가 내용을 수정한 결과를 저장
//    @PutMapping("/edit/{quizSeq}")
//    public void ctlUpdateQuiz(@PathVariable Long quizSeq, @RequestBody QuizDTO dto) {
//        quizService.svcUpdateQuiz(quizSeq, dto);
//    }
//
//    //특정 문제(quizSeq)를 삭제
//    @DeleteMapping("/delete/{quizSeq}")
//    public void ctlDeleteQuiz(@PathVariable Long quizSeq) {
//        quizService.svcDeleteQuiz(quizSeq);
//    }
//
//    //특정 시험(examSeq)에 등록된 문제들을 출제 완료 상태로 전환
//    @PostMapping("/publish/{examSeq}")
//    public void ctlPublishExam(@PathVariable Long examSeq) {
//        quizService.svcPublishExam(examSeq);
//    }

    // 1. 시험에 해당하는 문제 목록 조회
    @GetMapping("/list/{examSeq}")
    public Map<String, List<Map<String, Object>>> ctlGetQuizList(@PathVariable("examSeq") Long examSeq) {
        Map<String, List<Map<String, Object>>> resultMap = new HashMap<>();
        try {
            List<Map<String, Object>> quizList = quizService.svcGetQuizListMap(examSeq);
            resultMap.put("dsQuizList", quizList); // ✅ 데이터셋 이름과 일치
        } catch (Exception ex) {
            ex.printStackTrace();
            resultMap.put("dsQuizList", Collections.emptyList());
        }

        return resultMap;
    }

    // 2. 문제 수정 (Map 기반)
    @PutMapping("/edit/{quizSeq}")
    public void ctlUpdateQuiz(@PathVariable("quizSeq") Long quizSeq, @RequestParam Map<String, Object> params) {
        quizService.svcUpdateQuizMap(quizSeq, params);
    }

    // 출제 완료: 전체 문제 저장
    @PutMapping("/save-all")
    public void ctlSaveAllQuiz(@RequestBody List<QuizDTO> quizList) {
        quizService.svcSaveAllQuiz(quizList);
    }

    // 3. 문제 삭제
    @DeleteMapping("/delete/{quizSeq}")
    public void ctlDeleteQuiz(@PathVariable("quizSeq") Long quizSeq) {
        quizService.svcDeleteQuiz(quizSeq);
    }

    // 4. 시험 출제 완료
    @PostMapping("/publish/{examSeq}")
    public void ctlPublishExam(@PathVariable("examSeq") Long examSeq) {
        quizService.svcPublishExam(examSeq);
    }
}

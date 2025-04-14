package com.unique.controller;

import com.unique.dto.AnswerDTO;
import com.unique.dto.AnswerDetailDTO;
import com.unique.entity.AnswerEntity;
import com.unique.service.AnswerService;
import com.unique.service.AnswerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class AnswerRestController {
    private final AnswerServiceImpl answerService;

    //------------------------------- 수민 조인 샘플 예시 ---------------------------
//    @GetMapping("/test")
//    public ResponseEntity<List<TestDTO>> ctlExamResult() {
//        List<TestDTO> result = answerService.svcTest();
//        return ResponseEntity.ok(result);
//    }
    //----------------------------------------------------------------------------
    @GetMapping("/answers")
    public ResponseEntity<List<AnswerEntity>> ctlGetAllAnswers() {
        return ResponseEntity.ok(answerService.svcGetAllAnswers());
    }

    @GetMapping("/answers/{userid}")
    public ResponseEntity<Optional<AnswerEntity>> ctlGetAnswer(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(answerService.svcGetAnswer(id));
    }

    @PostMapping("/answers")
    public void ctlCreateAnswer(@RequestBody AnswerEntity answer) {
        answerService.svcCreateAnswer(answer);
    }

    @PutMapping("/answers")
    public void ctlUpdateAnswer(@RequestBody AnswerEntity answer) {
        answerService.svcUpdateAnswer(answer);
    }

    @DeleteMapping("/answers/{userid}")
    public void ctlDeleteAnswer(@PathVariable(value="id") Long id) {
        answerService.svcDeleteAnswer(id);
    }

    //응시자 답안 확인
//    @GetMapping("/memberlist")
//    public ResponseEntity<List<AnswerDTO>> ctlGetAllMembers() {
//        return ResponseEntity.ok(answerService.findAnswerWithApplysMemberAndQuiz());
//    }
    @GetMapping("/memberlist")
    public ResponseEntity<List<AnswerDTO>> ctlFindAnswerWithMemberAndQuiz() {
        List<AnswerDTO> result = answerService.svcFindAnswerWithMemberAndQuiz();
        return ResponseEntity.ok(result);
    }

    //임의의 학생 시험 결과 확인 userid = 20220293로 호출
    @GetMapping("/result/{userid}")
    public ResponseEntity<List<AnswerDetailDTO>> ctlFindSelectedStudentResult(@PathVariable Long userid) {
        List<AnswerDetailDTO> result = answerService.svcFindSelectedStudentResult(userid);
        return ResponseEntity.ok(result);
    }



}


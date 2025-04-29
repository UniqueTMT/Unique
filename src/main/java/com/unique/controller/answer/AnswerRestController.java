package com.unique.controller.answer;

import com.unique.dto.answer.AnswerDTO;
import com.unique.dto.answer.AnswerDetailDTO;
import com.unique.dto.answer.StudentExamResultDTO;
import com.unique.entity.answer.AnswerEntity;
import com.unique.impl.answer.AnswerServiceImpl;
import com.unique.kafka.AnswerConfirmDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/answer")
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

    @PutMapping("/grade/confirm")
    public ResponseEntity<String> confirmGrading(@RequestBody AnswerConfirmDTO dto) {
        answerService.confirmGrading(dto);
        return ResponseEntity.ok("교수 2차 채점 완료");
    }

    @DeleteMapping("/answers/{userid}")
    public void ctlDeleteAnswer(@PathVariable(value="id") Long id) {
        answerService.svcDeleteAnswer(id);
    }

    //응시자 답안 확인
//    @GetMapping("/memberlist")
//    public ResponseEntity<List<AnswerDTO>> ctlGetAllMembersAnswers() {
//        return ResponseEntity.ok(answerService.findAnswerWithApplysMemberAndQuiz());
//    }
    @GetMapping("/memberlist")
    public ResponseEntity<List<AnswerDTO>> ctlGetAllMembersAnswers() {
        List<AnswerDTO> result = answerService.svcGetAllMembersAnswers();
        return ResponseEntity.ok(result);
    }

    //임의의 학생 시험 결과 확인 userid = 20220293로 호출
    @GetMapping("/result/{userid}")
    public ResponseEntity<List<AnswerDetailDTO>> ctlFindSelectedStudentResult(@PathVariable Long userid) {
        List<AnswerDetailDTO> result = answerService.svcFindSelectedStudentResult(userid);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/student-results/{userid}")
    public ResponseEntity<List<StudentExamResultDTO>> ctlFindStudentExamResultsByUserid(
            @PathVariable Long userid
    ) {
        List<StudentExamResultDTO> result = answerService.svcFindStudentExamResultsByUserid(userid);
        return ResponseEntity.ok(result);
    }

}


package com.unique.controller.answer;

import com.unique.dto.answer.AnswerDTO;
import com.unique.dto.answer.AnswerDetailDTO;
import com.unique.dto.answer.AnswerSubmitDTO;
import com.unique.dto.answer.StudentExamResultDTO;
import com.unique.dto.answer.WrapperAnswerSubmitDTO;
import com.unique.dto.applys.ApplyCheckDTO;
import com.unique.entity.answer.AnswerEntity;
import com.unique.entity.applys.ApplysEntity;
import com.unique.impl.answer.AnswerServiceImpl;
import com.unique.kafka.AnswerConfirmDTO;
import com.unique.repository.applys.ApplysRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/answer")
@RequiredArgsConstructor
public class AnswerRestController {
    private final AnswerServiceImpl answerService;
    private final ApplysRepository applysRepository;

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
    public ResponseEntity<Map<String, String>> confirmGrading(@RequestBody AnswerConfirmDTO dto) {
        answerService.confirmGrading(dto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "모든 2차 채점이 완료되었습니다.");
        return ResponseEntity.ok(response); // Content-Type: application/json
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
//    @GetMapping("/memberlist")
//    public ResponseEntity<List<AnswerDTO>> ctlGetAllMembersAnswers() {
//        List<AnswerDTO> result = answerService.svcGetAllMembersAnswers();
//        return ResponseEntity.ok(result);
//    }
//
//    //임의의 학생 시험 결과 확인 userid = 20220293로 호출
//    @GetMapping("/result/{userid}")
//    public ResponseEntity<List<AnswerDetailDTO>> ctlFindSelectedStudentResult(@PathVariable Long userid) {
//        List<AnswerDetailDTO> result = answerService.svcFindSelectedStudentResult(userid);
//        return ResponseEntity.ok(result);
//    }
//
//    @GetMapping("/student-results/{userid}")
//    public ResponseEntity<List<StudentExamResultDTO>> ctlFindStudentExamResultsByUserid(
//            @PathVariable Long userid
//    ) {
//        List<StudentExamResultDTO> result = answerService.svcFindStudentExamResultsByUserid(userid);
//        return ResponseEntity.ok(result);
//    }

    @GetMapping("/result/memberlist")
    public ResponseEntity<Map<String, Object>> ctlGetAllMembersAnswers() {
        List<AnswerDTO> result = answerService.svcGetAllMembersAnswers(); // JOIN 결과 조회
        Map<String, Object> map = new HashMap<>();
        map.put("dsAnswer", result);
        return ResponseEntity.ok(map);
    }

    //임의의 학생 시험 결과 확인 userid = 20251111로 호출
    @GetMapping("/result/{userid}")
    public ResponseEntity<Map<String, Object>> ctlFindSelectedStudentResult(@PathVariable("userid") Long userid) {
        List<AnswerDetailDTO> result = answerService.svcFindSelectedStudentResult(userid);
        Map<String, Object> map = new HashMap<>();
        map.put("dsAnswerList", result);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/student-results/{userid}")
    public ResponseEntity<Map<String, Object>> ctlFindStudentExamResultsByUserid(
            @PathVariable("userid") Long userid
    ) {
        List<StudentExamResultDTO> result = answerService.svcFindStudentExamResultsByUserid(userid);
        Map<String, Object> map = new HashMap<>();
        map.put("dsStudentResult", result);
        return ResponseEntity.ok(map);
    }

    // 응시자 1차 채점 확인
    @GetMapping("/grading-detail")
    public Map<String, Object> getGradingDetail(@RequestParam Long userSeq, @RequestParam Long roomSeq) {
        Map<String, Object> result = new HashMap<>();
        result.put("answerList", answerService.getGradingDetail(userSeq, roomSeq));
        return result;
    }

    // 답안 제출 및 채점
    @PostMapping("/submit")
    public ResponseEntity<?> submitAnswers(@RequestBody WrapperAnswerSubmitDTO wrapperDTO) {
        if (wrapperDTO == null || wrapperDTO.getData() == null) {
            return ResponseEntity.badRequest().body("❌ data 래핑된 구조가 없습니다.");
        }

        AnswerSubmitDTO submitDTO = wrapperDTO.getData();
        System.out.println("🟢 받은 전체 데이터: " + submitDTO);

        Long userSeq = submitDTO.getUserSeq();
        Long roomSeq = submitDTO.getRoomSeq();

        if (userSeq == null || roomSeq == null) {
            return ResponseEntity.badRequest().body("❌ userSeq 또는 roomSeq 누락됨");
        }

        Optional<ApplysEntity> applysOpt = applysRepository.findByUserSeqAndRoomSeq(userSeq, roomSeq);
        if (applysOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("❌ 응시 내역이 존재하지 않습니다.");
        }

        answerService.saveAllAnswers(submitDTO, applysOpt.get().getApplysSeq());
        return ResponseEntity.ok("✅ 답안 제출 및 채점 완료");
    }



}


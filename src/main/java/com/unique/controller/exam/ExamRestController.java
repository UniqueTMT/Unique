package com.unique.controller.exam;

import com.unique.dto.exam.ExamDTO;
import com.unique.dto.answer.AnswerDetailDTO;
import com.unique.dto.exam.CategoryQuizCountDTO;
import com.unique.dto.exam.ExamDetailDTO;
import com.unique.dto.exam.ExamParticipationDTO;
import com.unique.dto.room.OpenRoomDTO;
import com.unique.entity.exam.ExamEntity;
import com.unique.impl.exam.ExamServiceImpl;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/exam")
@RequiredArgsConstructor
public class ExamRestController {
    private final ExamServiceImpl examService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, List<AnswerDetailDTO>>> ctlFindAll() {
        List<AnswerDetailDTO> list = examService.svcFindAll();

        Map<String, List<AnswerDetailDTO>> resultMap = new HashMap<>();
        resultMap.put("subRoomList", list); // 원하는 키값으로 변경 가능

        return ResponseEntity.ok(resultMap);
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

    //문제은행 리스트업
//    @GetMapping("/quizbank-list")
//    public ResponseEntity<List<CategoryQuizCountDTO>> ctlGetQuizCountByCategory() {
//        return ResponseEntity.ok(examService.svcGetQuizCountByCategory());
//    }
//
//    //문제은행 카테고리별 시험지 상세 보기
//    @GetMapping("/quizbank-detail")
//    public ResponseEntity<List<ExamDTO>> ctlFindExamWithQuizList() {
//        return ResponseEntity.ok(examService.svcFindExamWithQuizList());
//    }

    //문제은행 리스트업
    @GetMapping("/quizbank-list")
    public ResponseEntity<Map<String, List<CategoryQuizCountDTO>>> ctlGetQuizCountByCategory() {
        List<CategoryQuizCountDTO> quizCountList = examService.svcGetQuizCountByCategory();

        Map<String, List<CategoryQuizCountDTO>> map = new HashMap<>();
        map.put("dsQuizCount", quizCountList); // eXbuilder6에서 바인딩할 키 이름

        return ResponseEntity.ok()
                .header("Access-Control-Expose-Headers", "Content-Disposition")
                .body(map);
    }


    //문제은행 카테고리별 시험지 상세 보기
    @GetMapping("/quizbank-detail/{subjectCode}")
    public ResponseEntity<Map<String, List<ExamDetailDTO>>> ctlFindExamWithQuizList(@PathVariable("subjectCode") String subjectCode) {
        List<ExamDetailDTO> examList = examService.svcFindExamWithQuizListBySubjectCode(subjectCode);

        Map<String, List<ExamDetailDTO>> map = new HashMap<>();
        map.put("dsQuizList", examList);

        return ResponseEntity.ok()
                .header("Access-Control-Expose-Headers", "Content-Disposition")
                .body(map);
    }


  // 유저가 생성한 시험지 조회
  @GetMapping("/create-examList")
  public ResponseEntity<Map<String, Object>> getMySubjectList(Long userSeq) {

    List<ExamEntity> list = examService.getSubjectListByLoginUser(userSeq);

    Map<String, Object> result = new HashMap<>();
    result.put("examList", list);

    return ResponseEntity.ok(result);
  }
}
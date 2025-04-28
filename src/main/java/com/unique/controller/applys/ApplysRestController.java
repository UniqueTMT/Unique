package com.unique.controller.applys;

import com.unique.dto.member.MemberExamHistoryDTO;
import com.unique.dto.member.MemberExamHistoryDetailDTO;
import com.unique.entity.applys.ApplysEntity;
import com.unique.impl.applys.ApplysServiceImpl;
import com.unique.impl.member.MemberExamHistoryDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/applys")
@RequiredArgsConstructor
public class ApplysRestController {
    private final ApplysServiceImpl applysServiceImpl;
    private final MemberExamHistoryDetailServiceImpl userExamHistoryDetailServiceImpl;
    private final ApplysServiceImpl applysService;

//    @GetMapping("/applys")
//    public ResponseEntity<List<ApplysEntity>> ctlApplysList() {
//        return ResponseEntity.ok(applysService.svcApplysList());
//    }
//
//    @GetMapping("/applys/{id}")
//    public ResponseEntity<Optional<ApplysEntity>> ctlApplysDetail(@PathVariable(value="id") Long id) {
//        return ResponseEntity.ok(applysService.svcApplysDetail(id));
//    }

//    @PostMapping("/applys")
//    public void ctlApplysInsert(@RequestBody ApplysEntity entity) {
//        applysService.svcApplysInsert(entity);
//    }
//
//    @PutMapping("/applys")
//    public void ctlApplysUpdate(@RequestBody ApplysEntity entity) {
//        applysService.svcApplysUpdate(entity);
//    }
//
//    @DeleteMapping("/applys/{id}")
//    public void ctlApplysDelete(@PathVariable(value="id") Long id) {
//        applysService.svcApplysDelete(id);
//    }


    /*
     * function : 유저 응시 시험 리스트 결과
     * author : 차경준
     * regdate : 25.04.15
     * */
    @GetMapping("/apply-history/{userSeq}")
    public ResponseEntity<Map<String,List<MemberExamHistoryDTO>>> ctlFindAllExamHistory(@PathVariable(value = "userSeq") Long userSeq) {
        HashMap<String, List<MemberExamHistoryDTO>> map = new HashMap<>();
        map.put("ds1",applysService.myFindAllExamHistory(userSeq));
        return ResponseEntity.ok()
                .header("Access-Control-Expose-Headers", "Content-Disposition")
                .body(map);
    }


    /*
     * function : 유저 응시 시험 리스트 결과 (정렬)
     * author : 차경준
     * regdate : 25.04.15
     * */
    @GetMapping("/apply-history/{userSeq}/sorted")
    public ResponseEntity<List<MemberExamHistoryDTO>> ctlExamHistorySorted(
            @PathVariable("userSeq") Long userSeq,
            @RequestParam(defaultValue = "desc") String sort) {
        return ResponseEntity.ok(applysService.svcExamHistorySorted(userSeq, sort));
    }


    /*
    * function : 유저 응시 시험 세부 결과
    * author : 차경준
    * regdate : 25.04.15
    * */
    @GetMapping("/apply-history/{userSeq}/{examSeq}")
    public ResponseEntity<MemberExamHistoryDetailDTO> ctlGetExamResult(
            @PathVariable(value = "userSeq") Long userSeq,
            @PathVariable(value = "examSeq") Long examSeq
    ) {
        try {
            MemberExamHistoryDetailDTO result = userExamHistoryDetailServiceImpl.svcGetExamResult(userSeq, examSeq);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("Error fetching exam result: " + e.getMessage());
            e.printStackTrace(); // 예외 내용을 출력
            return ResponseEntity.notFound().build();
        }
    }


    /*
    * function : 유저 오답노트 세부 결과
    * author : 경준
    * regdate : 25.04.17
    * */
    @GetMapping("/apply-history-wrong/{userSeq}/{examSeq}")
    public ResponseEntity<MemberExamHistoryDetailDTO> ctlGetExamResultWrongAnswers(
            @PathVariable(value = "userSeq") Long userSeq,
            @PathVariable(value = "examSeq") Long examSeq
    ) {
        try {
            MemberExamHistoryDetailDTO result = userExamHistoryDetailServiceImpl.svcGetExamResultWrongAnswers(userSeq, examSeq);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("Error fetching exam result: " + e.getMessage());
            e.printStackTrace(); // 예외 내용을 출력
            return ResponseEntity.notFound().build();
        }
    }


    //시험이력 검색기능 - 경준
//    @GetMapping("/search/{userSeq}")
//    public ResponseEntity<List<MemberExamHistoryDTO>> ctlSearchUserExamHistory(
//            @PathVariable Long userSeq,
//            @RequestParam(required = false) String subjectName,
//            @RequestParam(required = false) String creatorName,
//            @RequestParam(required = false) String examTitle) {
//
//        List<MemberExamHistoryDTO> result =
//                applysService.svcSearchUserExamHistory(userSeq, subjectName, creatorName, examTitle);
//
//        return ResponseEntity.ok(result);
//    }

    @GetMapping("/search/{userSeq}")
    public ResponseEntity<Map<String, Object>> ctlSearchUserExamHistory(
            @PathVariable Long userSeq,
            @RequestParam(required = false) String subjectName,
            @RequestParam(required = false) String creatorName,
            @RequestParam(required = false) String examTitle) {

        List<MemberExamHistoryDTO> result =
                applysService.svcSearchUserExamHistory(userSeq, subjectName, creatorName, examTitle);

        Map<String, Object> response = new HashMap<>();
        response.put("dm1", result); // 검색 결과를 dm1 키에 담음

        return ResponseEntity.ok()
                .header("Access-Control-Expose-Headers", "Content-Disposition")
                .body(response);
    }
}

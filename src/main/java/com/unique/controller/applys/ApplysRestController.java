package com.unique.controller.applys;

import com.unique.dto.member.MemberExamHistoryDTO;
import com.unique.dto.member.MemberExamHistoryDetailDTO;
import com.unique.entity.applys.ApplysEntity;
import com.unique.impl.applys.ApplysServiceImpl;
import com.unique.impl.member.MemberExamHistoryDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/applys")
@RequiredArgsConstructor
public class ApplysRestController {
    private final ApplysServiceImpl applysServiceImpl;
    private final MemberExamHistoryDetailServiceImpl userExamHistoryDetailServiceImpl;
    private final ApplysServiceImpl applysService;

    @GetMapping("/applys")
    public ResponseEntity<List<ApplysEntity>> ctlApplysList() {
        return ResponseEntity.ok(applysService.svcApplysList());
    }

    @GetMapping("/applys/{id}")
    public ResponseEntity<Optional<ApplysEntity>> ctlApplysDetail(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(applysService.svcApplysDetail(id));
    }

    @PostMapping("/applys")
    public void ctlApplysInsert(@RequestBody ApplysEntity entity) {
        applysService.svcApplysInsert(entity);
    }

    @PutMapping("/applys")
    public void ctlApplysUpdate(@RequestBody ApplysEntity entity) {
        applysService.svcApplysUpdate(entity);
    }

    @DeleteMapping("/applys/{id}")
    public void ctlApplysDelete(@PathVariable(value="id") Long id) {
        applysService.svcApplysDelete(id);
    }

    //유저 응시 시험 리스트 - 경준
    @GetMapping("/apply-history/{userSeq}")
    public ResponseEntity<List<MemberExamHistoryDTO>> ctlFindAllExamHistory(@PathVariable(value = "userSeq") Long userSeq) {
        return ResponseEntity.ok(applysService.myFindAllExamHistory(userSeq));
    }

    //유저 응시 시험 세부 결과 - 경준
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


    //시험이력 검색기능 - 경준
    @GetMapping("/search/{userSeq}")
    public ResponseEntity<List<MemberExamHistoryDTO>> ctlSearchUserExamHistory(
            @PathVariable Long userSeq,
            @RequestParam(required = false) String subjectName,
            @RequestParam(required = false) String creatorName,
            @RequestParam(required = false) String examTitle) {

        List<MemberExamHistoryDTO> result =
                applysService.svcSearchUserExamHistory(userSeq, subjectName, creatorName, examTitle);

        return ResponseEntity.ok(result);
    }
}

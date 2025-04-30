package com.unique.controller.appeal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unique.dto.appeal.AppealDTO;
import com.unique.dto.appeal.AppealDetailDTO;
import com.unique.dto.appeal.AppealPostDTO;
import com.unique.dto.appeal.AppealScoreAdjustRequestDTO;
import com.unique.entity.appeal.AppealEntity;
import com.unique.impl.appeal.AppealServiceImpl;
import com.unique.repository.appeal.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/appeal")

@RequiredArgsConstructor
public class AppealRestController {
    private final AppealServiceImpl appealService;
    private final SseEmitterRepository sseEmitterRepository;

    @PostMapping("/appeal")
    public void ctlAppealInsert(@RequestBody AppealEntity entity) {
        appealService.svcAppealInsert(entity);
    }

//    @PostMapping("/board")
//    public ResponseEntity<?> indexBoard(DataRequest dataRequest) {
//
//        ParameterGroup parameterGroup = dataRequest.getParameterGroup("id");
//        Entity beanData = (Entity) parameterGroup.getBeanData(Entity.class);
//    }


    /*
     * function : 이의제기 점수 수정 - 작성중
     * author : 차경준
     * regdate : 2025.04.15
     * */
    @PutMapping("/{appealSeq}/adjust")
    public ResponseEntity<String> ctlAppealAcceptScoreUpdate(
            @PathVariable Long appealSeq,
            @RequestParam Long quizSeq
    ) {
        appealService.svcAppealUpdate(appealSeq,quizSeq);
        return ResponseEntity.ok("성적 수정 완료");
    }



    /*
    * function : 교수 이의제기 리스트
    * author : 차경준
    * regdate : 2025.04.15
    * */
    @GetMapping("/appealList")
    public ResponseEntity<Map<String, List<AppealDTO>>> ctlAppealList() {
        HashMap<String, List<AppealDTO>> map = new HashMap<>();
        map.put("dsAppealList", appealService.svcAppealList());
        return ResponseEntity.ok()
                .header("Access-Control-Expose-Headers", "Content-Disposition")
                .body(map);
    }
    
    /*
     * function : 이의제기 세부 결과 - 아직 교수 번호 하드코딩 안해놓음.
     * author : 차경준
     * regdate : 2025.04.15
     * */
    @GetMapping("/detail/{appealSeq}")
    public ResponseEntity<AppealDetailDTO> ctlAppealDetail(@PathVariable Long appealSeq) {
        return ResponseEntity.ok(appealService.svcAppealDetail(appealSeq));
    }

    /*
     * function : 이의제기 삭제
     * author : 차경준
     * regdate : 2025.04.15
     * */
    @DeleteMapping("/{id}")
    public void ctlAppealDelete(@PathVariable(value="id") Long id) {
        appealService.svcAppealDelete(id);
    }


    /*
     * function : 이의제기 생성
     * author : 차경준
     * regdate : 2025.04.15
     * */
//    @PostMapping("/create-appeal")
//    public ResponseEntity<String> ctlAppealInsert(@RequestBody AppealPostDTO appealDTO) {
//        try {
//            appealService.svcAppealCreate(appealDTO);
//            return ResponseEntity.ok("이의제기가 등록되었습니다.");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @PostMapping("/create-appeal")
    public ResponseEntity<Map<String, Object>> ctlAppealInsert(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> response = new HashMap<>();
        try {
            // "data" → "dmAppeal" 추출
            Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
            Map<String, Object> dmAppealMap = (Map<String, Object>) dataMap.get("dmAppeal");
            ObjectMapper mapper = new ObjectMapper();
            AppealPostDTO appealDTO = mapper.convertValue(dmAppealMap, AppealPostDTO.class);

            AppealDTO savedAppeal = appealService.svcAppealCreate(appealDTO);
            response.put("dmAppeal", savedAppeal);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("dmAppeal", null);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


    /*
     * function : 교수용 SSE 구독 엔드포인트 (알림용)
     * author : 차경준
     * regdate : 2025.04.15
     * */
    @GetMapping("/subscribe/{userSeq}")
    public SseEmitter subscribe(@PathVariable Long userSeq) {
        return appealService.subscribe(userSeq);
    }


}

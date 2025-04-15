package com.unique.controller.appeal;

import com.unique.dto.appeal.AppealDTO;
import com.unique.dto.appeal.AppealPostDTO;
import com.unique.entity.appeal.AppealEntity;
import com.unique.impl.appeal.AppealServiceImpl;
import com.unique.repository.appeal.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appeal")

@RequiredArgsConstructor
public class AppealRestController {
    private final AppealServiceImpl appealService;
    private final SseEmitterRepository sseEmitterRepository;

    /*
    * function : 교수 이의제기 리스트
    * author : 차경준
    * regdate : 2025.04.15
    * */
    @GetMapping("/appeal")
    public ResponseEntity<List<AppealDTO>> ctlAppealList() {
        return ResponseEntity.ok(appealService.svcAppealList());
    }

    @GetMapping("/appeal/{id}")
    public ResponseEntity<Optional<AppealEntity>> ctlAppealDetail(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(appealService.svcAppealDetail(id));
    }

    @PostMapping("/appeal")
    public void ctlAppealInsert(@RequestBody AppealEntity entity) {
        appealService.svcAppealInsert(entity);
    }

    @PutMapping("/appeal")
    public void ctlAppealUpdate(@RequestBody AppealEntity entity) {
        appealService.svcAppealUpdate(entity);
    }

    @DeleteMapping("/{id}")
    public void ctlAppealDelete(@PathVariable(value="id") Long id) {
        appealService.svcAppealDelete(id);
    }


    //유저 이의 제기 생성 -경준
    @PostMapping("/create-appeal")
    public ResponseEntity<String> ctlAppealInsert(@RequestBody AppealPostDTO appealDTO) {
        try {
            appealService.svcAppealCreate(appealDTO);
            return ResponseEntity.ok("이의제기가 등록되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // 교수용 SSE 구독 엔드포인트
    @GetMapping("/subscribe/{userSeq}")
    public SseEmitter subscribe(@PathVariable Long userSeq) {
        return appealService.subscribe(userSeq);
    }


}

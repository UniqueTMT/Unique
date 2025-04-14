package com.unique.controller.appeal;

import com.unique.dto.appeal.AppealPostDTO;
import com.unique.entity.appeal.AppealEntity;
import com.unique.impl.appeal.AppealServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appeal")

@RequiredArgsConstructor
public class AppealRestController {
    private final AppealServiceImpl appealService;

    @GetMapping("/appeal")
    public ResponseEntity<List<AppealEntity>> ctlAppealList() {
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

}

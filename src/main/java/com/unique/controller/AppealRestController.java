package com.unique.controller;

import com.unique.entity.AppealEntity;
import com.unique.service.AppealServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
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

    @DeleteMapping("/appeal/{id}")
    public void ctlAppealDelete(@PathVariable(value="id") Long id) {
        appealService.svcAppealDelete(id);
    }
}

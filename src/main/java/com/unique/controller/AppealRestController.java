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
    private final AppealServiceImpl appealServiceImpl;

    @GetMapping("/appeal")
    public ResponseEntity<List<AppealEntity>> ctlAppealList() {
        return ResponseEntity.ok(appealServiceImpl.svcAppealList());
    }

    @GetMapping("/appeal/{id}")
    public ResponseEntity<Optional<AppealEntity>> ctlAppealDetail(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(appealServiceImpl.svcAppealDetail(id));
    }

    @PostMapping("/appeal")
    public void ctlAppealInsert(@RequestBody AppealEntity entity) {
        appealServiceImpl.svcAppealInsert(entity);
    }

    @PutMapping("/appeal")
    public void ctlAppealUpdate(@RequestBody AppealEntity entity) {
        appealServiceImpl.svcAppealUpdate(entity);
    }

    @DeleteMapping("/appeal/{id}")
    public void ctlAppealDelete(@PathVariable(value="id") Long id) {
        appealServiceImpl.svcAppealDelete(id);
    }
}

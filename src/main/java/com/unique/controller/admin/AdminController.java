package com.unique.controller.admin;

import com.unique.dto.admin.*;
import com.unique.service.admin.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 1-1. 카테고리별 전체 시험 수
    @GetMapping("/statistics/exam-count")
    @Operation(summary = "카테고리별 전체 시험 수 조회", description = "과목별 등록된 시험 수를 반환합니다.")
    public List<AdminExamStatsDTO> getExamStats() {
        return adminService.getExamStatsByCategory();
    }

    // 1-2. 월별 시험 응시 추이
    @GetMapping("/statistics/exam-monthly")
    @Operation(summary = "월별 시험 응시 추이", description = "과목별 월 단위 시험 응시 횟수를 조회합니다.")
    public List<AdminExamMonthlyStatsDTO> getMonthlyStats() {
        return adminService.getMonthlyApplyStats();
    }

    // 1-3. 최근 시험 목록
    @GetMapping("/statistics/recent-exams")
    @Operation(summary = "최근 생성된 시험 목록", description = "최신 등록된 시험지를 조회합니다.")
    public List<AdminRecentExamDTO> getRecentExams() {
        return adminService.getRecentExams();
    }

    // 2-1. 진행 중인 시험방 수
    @GetMapping("/rooms/active-count")
    @Operation(summary = "진행 중인 시험방 수", description = "현재 진행 중인 시험방의 개수를 조회합니다.")
    public AdminRoomCountDTO getRunningRoomCount() {
        return adminService.getRunningRoomCount();
    }

    // 2-2. 시험방 목록
    @GetMapping("/rooms")
    @Operation(summary = "시험방 목록", description = "등록된 전체 시험방 목록을 조회합니다.")
    public List<AdminRoomDTO> getRoomList() {
        return adminService.getRoomList();
    }

    // 3. 회원 접속 로그
    @GetMapping("/logs/user-log")
    @Operation(summary = "회원 접속 로그", description = "회원의 접속 활동 로그를 조회합니다.")
    public List<AdminUserLogDTO> getUserLogs() {
        return adminService.getUserLogs();
    }
}

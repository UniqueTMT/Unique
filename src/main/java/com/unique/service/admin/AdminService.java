package com.unique.service.admin;

import com.unique.dto.admin.*;

import java.util.List;

public interface AdminService {

    // 1-1. 카테고리별 전체 시험 수 조회
    List<AdminExamStatsDTO> getExamStatsByCategory();

    // 1-2. 카테고리별 월 기준 시험 응시 추이
    List<AdminExamMonthlyStatsDTO> getMonthlyApplyStats();

    // 1-3. 최근 생성된 시험 목록
    List<AdminRecentExamDTO> getRecentExams();

    // 2-1. 진행 중인 시험방 수
    AdminRoomCountDTO getRunningRoomCount();

    // 2-2. 시험방 목록 조회
    List<AdminRoomDTO> getRoomList();

    // 3. 회원 접속 로그 조회
    List<AdminUserLogDTO> getUserLogs();
}

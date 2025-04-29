package com.unique.service.admin;

import com.unique.dto.admin.*;

import java.util.List;

public interface AdminService {

    /**
     * 카테고리별 전체 시험 수 조회
     * @return List of AdminExamStatsDTO
     */
    List<AdminExamStatsDTO> svcGetExamStatsByCategory();

    /**
     * 카테고리별 월별 시험 응시 추이 조회
     * @return List of AdminExamMonthDTO
     */
    List<AdminExamMonthDTO> svcGetMonthlyApplyStats();

    /**
     * 최근 생성된 시험 목록 조회
     * @return List of AdminRecentExamDTO
     */
    List<AdminRecentExamDTO> svcGetRecentExams();

    /**
     * 현재 진행 중인 시험방 수 조회
     * @return AdminRoomCountDTO
     */
    AdminRoomCountDTO svcGetRunningRoomCount();

    /**
     * 시험방 목록 조회
     * @return List of AdminRoomDTO
     */
    List<AdminRoomDTO> svcGetRoomList();

    /**
     * 회원 접속 로그 조회
     * @return List of AdminUserLogDTO
     */
    List<AdminUserLogDTO> svcGetUserLogs();
}

package com.unique.repository.admin;

import com.unique.dto.admin.*;

import com.unique.entity.exam.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<ExamEntity, Long> {

    // 1-1. 카테고리별 전체 시험 수
    @Query("SELECT new com.unique.dto.admin.AdminExamStatsDTO(e.subjectName, e.subjectCode, COUNT(e)) " +
            "FROM ExamEntity e " +
            "GROUP BY e.subjectName, e.subjectCode")
    List<AdminExamStatsDTO> getExamStatsByCategory();

    // 1-2. 카테고리별 월 기준 시험 응시 추이
    @Query("SELECT new com.unique.dto.admin.AdminExamMonthlyStatsDTO(e.subjectName, e.subjectCode, a.exam.examSeq, " +
            "FUNCTION('TO_CHAR', a.regdate, 'YYYY-MM'), COUNT(a)) " +
            "FROM ApplysEntity a " +
            "JOIN a.exam e " +
            "GROUP BY e.subjectName, e.subjectCode, a.exam.examSeq, FUNCTION('TO_CHAR', a.regdate, 'YYYY-MM') " +
            "ORDER BY FUNCTION('TO_CHAR', a.regdate, 'YYYY-MM')")
    List<AdminExamMonthlyStatsDTO> getMonthlyApplyStats();

    // 1-3. 최근 생성된 시험 목록
    @Query("SELECT new com.unique.dto.admin.AdminRecentExamDTO(e.subjectName, e.regdate, e.examTitle) " +
            "FROM ExamEntity e " +
            "ORDER BY e.regdate DESC")
    List<AdminRecentExamDTO> getRecentExams();

    // 2-1. 현재 열려 있는 시험방 수 (진행중 상태만)
    @Query("SELECT COUNT(r) FROM RoomEntity r WHERE r.roomStatus = '진행중'")
    Long getRunningRoomCount();

    // 2-2. 시험방 목록
    @Query("SELECT new com.unique.dto.admin.AdminRoomDTO(r.roomName, r.roomStatus, r.regdate, r.shutdownYn) " +
            "FROM RoomEntity r")
    List<AdminRoomDTO> getRoomList();

    // 3. 회원 접속 로그 (회원 + 로그 조인)
    @Query("SELECT new com.unique.dto.admin.AdminUserLogDTO(m.userid, l.logActive, l.regdate, l.userIp) " +
            "FROM MemberLogEntity l " +
            "JOIN l.member m " +
            "ORDER BY l.regdate DESC")
    List<AdminUserLogDTO> getUserLogs();
}

package com.unique.repository.admin;

import com.unique.dto.admin.AdminExamMonthDTO;
import com.unique.entity.applys.ApplysEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AdminExamMonthlyStatsRepository extends JpaRepository<ApplysEntity, Long> {

    /**
     * [1-2] 카테고리별 월 기준 시험 응시 추이
     */
    @Query("SELECT new com.unique.dto.admin.AdminExamMonthDTO(e.subjectName, e.subjectCode, " +
            "FUNCTION('TO_CHAR', a.regdate, 'YYYY-MM'), COUNT(a)) " +
            "FROM ApplysEntity a " +
            "JOIN a.exam e " +
            "GROUP BY e.subjectName, e.subjectCode, FUNCTION('TO_CHAR', a.regdate, 'YYYY-MM') " +
            "ORDER BY FUNCTION('TO_CHAR', a.regdate, 'YYYY-MM')")
    List<AdminExamMonthDTO> getMonthlyApplyStats();
}

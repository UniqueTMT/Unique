package com.unique.repository.admin;

import com.unique.dto.admin.AdminExamStatsDTO;
import com.unique.entity.exam.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminExamStatsRepository extends JpaRepository<ExamEntity, Long> {

    /**
     * [1-1] 카테고리별 전체 시험 수
     */
    @Query("SELECT new com.unique.dto.admin.AdminExamStatsDTO(e.subjectName, e.subjectCode, COUNT(e)) " +
            "FROM ExamEntity e " +
            "GROUP BY e.subjectName, e.subjectCode")
    List<AdminExamStatsDTO> getExamStatsByCategory();

}

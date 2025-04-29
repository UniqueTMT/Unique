package com.unique.repository.admin;

import com.unique.dto.admin.AdminRecentExamDTO;
import com.unique.entity.exam.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AdminRecentExamRepository extends JpaRepository<ExamEntity, Long> {

    /**
     * [1-3] 최근 생성된 시험 목록
     */
    @Query("SELECT new com.unique.dto.admin.AdminRecentExamDTO(e.subjectName, e.regdate, e.examTitle) " +
            "FROM ExamEntity e ORDER BY e.regdate DESC")
    List<AdminRecentExamDTO> getRecentExams();
}

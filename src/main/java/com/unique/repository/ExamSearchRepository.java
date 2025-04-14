package com.unique.repository;

import com.unique.entity.ApplysEntity;
import com.unique.entity.ExamEntity;
import jakarta.persistence.OrderBy;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamSearchRepository extends JpaRepository<ApplysEntity, Long> {


    /**
     * 사용자의 시험 이력 검색 (OR 조건) - 경준
     * @EntityGraph로 exam, exam.member 즉시 로딩
     */
    @EntityGraph(attributePaths = {"exam", "exam.member"})
    @Query("""
        SELECT a 
        FROM ApplysEntity a 
        JOIN a.exam e 
        JOIN e.member creator 
        WHERE a.member.userSeq = :userSeq
          AND (
              (:subjectName IS NOT NULL AND e.subjectName LIKE %:subjectName%) 
              OR (:creatorName IS NOT NULL AND creator.username LIKE %:creatorName%) 
              OR (:examTitle IS NOT NULL AND e.examTitle LIKE %:examTitle%)
          )
    """)
    List<ApplysEntity> mySearchUserExamHistory(
            @Param("userSeq") Long userSeq,
            @Param("subjectName") String subjectName,
            @Param("creatorName") String creatorName,
            @Param("examTitle") String examTitle
    );

}
package com.unique.repository.exam;

import com.unique.dto.exam.CategoryQuizCountDTO;
import com.unique.dto.answer.StudentExamResultDTO;
import com.unique.entity.exam.ExamEntity;
import jakarta.persistence.OrderBy;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<ExamEntity, Long> {


    @EntityGraph(attributePaths = {"quizList"}, type = EntityGraph.EntityGraphType.LOAD)
    @BatchSize(size = 10)
    @OrderBy("regdate ASC")
    List<ExamEntity> findAll();

    //문제은행 카테고리별 시험지 상세 보기
    @EntityGraph(attributePaths = {
            "quizList.quiz"
    })
    @Query("SELECT e FROM ExamEntity e")
    List<ExamEntity> findExamWithQuizList();

    //문제은행 리스트 업
    @Query("SELECT new com.unique.dto.exam.CategoryQuizCountDTO(e.subjectName, e.subjectCode, COUNT(q.quizSeq)) " +
            "FROM ExamEntity e JOIN e.quizList q " +
            "GROUP BY e.subjectName, e.subjectCode")
    List<CategoryQuizCountDTO> findQuizCountGroupedByCategory();

    // 유저가 생성한 시험지 조회
    @Query("SELECT e FROM ExamEntity e WHERE e.member.userSeq = :userSeq")
    List<ExamEntity> findByUserSeq(Long userSeq);


}
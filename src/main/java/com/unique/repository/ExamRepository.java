package com.unique.repository;

import com.unique.entity.ExamEntity;
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

//
//    시험지 조회
//    @EntityGraph(attributePaths = {
//            "quizList"
//    })
//    @Query("SELECT e FROM ExamEntity e")
//    List<ExamEntity> findExamWithQuizList();


}
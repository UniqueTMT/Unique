package com.unique.repository;

import com.unique.entity.AnswerEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {

    //샘플 코드 예외--------------------------------------------------------
    @EntityGraph(attributePaths = {
            "quiz",
            "quiz.exam",
            "applys",
            "applys.member"
    })
    @Query("SELECT a FROM AnswerEntity a")
    List<AnswerEntity> myFindExamResultsWithGraph();
}

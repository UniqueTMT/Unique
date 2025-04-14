package com.unique.repository.member;

import com.unique.entity.answer.AnswerEntity;
import com.unique.entity.applys.ApplysEntity;
import com.unique.entity.quiz.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 특정 시험 결과 데이터를 조회하는 레포지토리 클래스.
 */
@Repository
public interface MemberExamHistoryDetailRepository extends JpaRepository<ApplysEntity, Long> {
    


    /**
     * 특정 유저(userSeq)가 응시한 특정 시험(examSeq)의 데이터를 조회. - 경준
     * EntityGraph를 사용하여 연관 데이터를 효율적으로 로드.
     */
        // 1차 쿼리: ApplysEntity + Exam + Member (컬렉션은 로드하지 않음)
        @Query("""
        SELECT a 
        FROM ApplysEntity a 
        JOIN FETCH a.exam e 
        JOIN FETCH a.member m 
        WHERE m.userSeq = :userSeq AND e.examSeq = :examSeq
    """)
        ApplysEntity findApplysWithExamAndMember(@Param("userSeq") Long userSeq, @Param("examSeq") Long examSeq);

        // 2차 쿼리: Exam의 QuizList 조회
        @Query("""
        SELECT q 
        FROM QuizEntity q 
        WHERE q.exam.examSeq = :examSeq 
        ORDER BY q.quizSeq
    """)
        List<QuizEntity> findQuizzesByExamSeq(@Param("examSeq") Long examSeq);

        // 3차 쿼리: Applys의 AnswerList 조회
        @Query("""
        SELECT ans 
        FROM AnswerEntity ans 
        WHERE ans.applys.applysSeq = :applysSeq 
        ORDER BY ans.quiz.quizSeq
    """)
        List<AnswerEntity> findAnswersByApplysSeq(@Param("applysSeq") Long applysSeq);



}

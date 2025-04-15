package com.unique.repository.answer;

import com.unique.entity.answer.AnswerEntity;
import java.util.Optional;

import com.unique.entity.applys.ApplysEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {

    //임의의 학생 시험 결과 확인
    @EntityGraph(attributePaths = {
            "quiz",
            "quiz.exam",
            "applys",
            "applys.member"
    })
    @Query("SELECT a FROM AnswerEntity a WHERE a.applys.member.userid = :userid")
    List<AnswerEntity> findSelectedStudentResult(@Param("userid") Long userid);


    //응시자 답안 확인
    @EntityGraph(attributePaths = {
            "applys",
            "applys.member",
            "quiz"
    })
    @Query("SELECT a FROM AnswerEntity a")
    List<AnswerEntity> findGetAllMembersAnswers();
    

    // 응시 답안 제출 -> 저장 -> 채점
    Optional<AnswerEntity> findByApplys_ApplysSeqAndQuiz_QuizSeq(Long applysSeq, Long quizSeq);

    
    /*
    * function : 응시 기록 (Applys) 으로 답안 조회
    * author : 차경준
    * regdate : 25.04.15
    * */
    List<AnswerEntity> findByApplys(ApplysEntity applys);
}

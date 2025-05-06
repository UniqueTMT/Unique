package com.unique.repository.answer;

import com.unique.dto.answer.StudentExamResultDTO;
import com.unique.entity.answer.AnswerEntity;
import com.unique.dto.answer.AnswerGradingDetailDTO;
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

    @EntityGraph(attributePaths = {
            "applys",
            "applys.member",
            "quiz",
            "quiz.exam",
            "quiz.exam.room"
    })
    @Query("""
    SELECT new com.unique.dto.answer.StudentExamResultDTO(
        e.subjectName,
        r.roomName,
        a.professorScore,
        m.userid,
        m.username
    )
    FROM AnswerEntity a
    JOIN a.applys ap
    JOIN ap.member m
    JOIN a.quiz q
    JOIN q.exam e
    JOIN RoomEntity r ON r.exam.examSeq = e.examSeq
    WHERE m.userid = :userid
""")
    List<StudentExamResultDTO> findStudentExamResultsByUserid(@Param("userid") Long userid);


    /*
    * function : 응시 기록 (Applys) 으로 답안 조회
    * author : 차경준
    * regdate : 25.04.15
    * */
    List<AnswerEntity> findByApplys(ApplysEntity applys);

    /*
     * function : 응시 기록 및 답안 조회
     * author : 차경준
     * regdate : 25.04.15
     * */
    @Query("SELECT a FROM AnswerEntity a JOIN FETCH a.quiz WHERE a.applys = :applys")
    List<AnswerEntity> findByApplysWithQuiz(@Param("applys") ApplysEntity applys);

    /*
     * function : 응시 기록, 답안 조회
     * author : 차경준
     * regdate : 25.04.15
     * */
    @Query("SELECT a FROM AnswerEntity a WHERE a.applys = :applys AND a.quiz.quizSeq = :quizSeq")
    Optional<AnswerEntity> findByApplysAndQuizSeq(
            @Param("applys") ApplysEntity applys,
            @Param("quizSeq") Long quizSeq
    );

    // 유저가 만든 방 조회
    @Query("""
    SELECT a FROM AnswerEntity a
    JOIN a.applys ap
    JOIN ap.exam e
    WHERE e.examSeq = :examSeq
    """)
        List<AnswerEntity> findAllByRoomSeq(@Param("examSeq") Long examSeq);


    @Query("""
    SELECT new com.unique.dto.answer.AnswerGradingDetailDTO(
        a.answerSeq,
        a.quiz.quiz,
        a.userAnswer,
        a.aiScore,
        a.aiFeedback,
        a.confirmed,
        a.professorScore,
        a.professorFeedback
    )
    FROM AnswerEntity a
    WHERE a.applys.member.userSeq = :userSeq
      AND a.applys.exam.examSeq = (
            SELECT r.exam.examSeq FROM RoomEntity r WHERE r.roomSeq = :roomSeq
      )
    ORDER BY a.answerSeq
    """)
    List<AnswerGradingDetailDTO> findGradingDetailByUserAndRoom(
        @Param("userSeq") Long userSeq,
        @Param("roomSeq") Long roomSeq
    );


    Optional<AnswerEntity> findByApplys_ApplysSeqAndQuiz_QuizSeq(Long applysSeq, Long quizSeq);


}

package com.unique.repository;

import com.unique.dto.UserExamHistoryDTO;
import com.unique.entity.AnswerEntity;
import com.unique.entity.AppealEntity;
import com.unique.entity.ApplysEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ApplysRepository extends JpaRepository<ApplysEntity, Long> {

//    @EntityGraph(attributePaths = {"exam", "member", "exam.creator", "exam.quizList"})
//    @Query("""
//        SELECT new com.unique.dto.UserExamHistoryDTO(
//            e.examSeq,
//            e.subjectCode,
//            e.subjectName,
//            e.examTitle,
//            m.userSeq,
//            c.username AS creatorName,
//            m.userid,
//            m.username AS userName,
//            a.applysSeq,
//            SUM(q.correctScore) AS totalScore,
//            SUM(CASE WHEN ans.answerYn = '1' THEN q.correctScore ELSE 0 END) AS obtainedScore,
//            a.regdate
//        )
//        FROM ApplysEntity a
//        JOIN a.exam e
//        JOIN a.member m
//        JOIN e.creator c
//        JOIN e.quizList q
//        LEFT JOIN AnswerEntity ans ON q.quizSeq = ans.quizSeq AND ans.applys.applysSeq = a.applysSeq
//        WHERE m.userSeq = :userSeq
//        GROUP BY a.applysSeq, e.examSeq, e.subjectCode, e.subjectName, e.examTitle, m.userSeq, c.username, m.userId, m.username, a.regdate
//        ORDER BY a.regdate DESC
//    """)
//    List<UserExamHistoryDTO> myFindAllExamHistory(@org.springframework.data.repository.query.Param("userSeq") Long userSeq);
//}

}
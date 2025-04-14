package com.unique.repository;

import com.unique.dto.UserExamHistoryDTO;
import com.unique.entity.AnswerEntity;
import com.unique.entity.AppealEntity;
import com.unique.entity.ApplysEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ApplysRepository extends JpaRepository<ApplysEntity, Long> {
        
        
        //내 시험 이력 리스트 - 경준
        @EntityGraph(attributePaths = {"exam", "member", "exam.member", "exam.quizList"})
        @Query("""
            SELECT new com.unique.dto.UserExamHistoryDTO(
                e.examSeq,
                e.subjectCode,
                e.subjectName,
                e.examTitle,
                m.userSeq, 
                c.username,  
                m.userid, 
                m.username,  
                a.applysSeq,
                CAST(SUM(q.correctScore) AS integer), 
                CAST(SUM(CASE WHEN ans.answerYn = '1' THEN q.correctScore ELSE 0 END) AS integer),
                a.regdate
            )
            FROM ApplysEntity a
            JOIN a.exam e
            JOIN a.member m 
            JOIN e.member c 
            JOIN e.quizList q
            LEFT JOIN AnswerEntity ans ON ans.quiz.quizSeq = q.quizSeq AND ans.applys.applysSeq = a.applysSeq
            WHERE m.userSeq = :userSeq
            GROUP BY a.applysSeq, e.examSeq, e.subjectCode, e.subjectName, e.examTitle, 
                     m.userSeq, c.username, m.userid, m.username, a.regdate
            ORDER BY a.regdate DESC
        """)
        List<UserExamHistoryDTO> myFindAllExamHistory(@Param("userSeq") Long userSeq);


}

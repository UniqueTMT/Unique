package com.unique.repository.applys;

import com.unique.dto.member.MemberExamHistoryDTO;
import com.unique.entity.applys.ApplysEntity;
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
            SELECT new com.unique.dto.member.MemberExamHistoryDTO(
                e.examSeq,
                e.subjectCode,
                e.subjectName,
                e.examTitle,
                m.userSeq, 
                c.username,  
                m.userid, 
                m.username,  
                a.applysSeq,
                a.totalScore,
                a.correctCount,
                a.wrongCount,
                a.regdate
            )
               FROM ApplysEntity a
               JOIN a.exam e
               JOIN a.member m\s
               JOIN e.member c\s
               WHERE m.userSeq = :userSeq
               ORDER BY a.regdate DESC
        """)
        List<MemberExamHistoryDTO> myFindAllExamHistory(@Param("userSeq") Long userSeq);


}

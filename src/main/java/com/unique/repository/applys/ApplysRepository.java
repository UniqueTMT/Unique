package com.unique.repository.applys;

import com.unique.dto.applys.ApplysConfirmStatusDTO;
import com.unique.dto.member.MemberExamHistoryDTO;
import com.unique.entity.applys.ApplysEntity;
import java.util.Optional;
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

        // 시험 방 관리 - 응시한 유저 수 조회
        @Query("""
        SELECT new com.unique.dto.applys.ApplysConfirmStatusDTO(
            m.userSeq,
            m.nickname,
            CASE
                WHEN COUNT(a2) > 0 AND SUM(CASE WHEN a2.confirmed = false THEN 1 ELSE 0 END) = 0 THEN true
                ELSE false
            END
        )
        FROM ApplysEntity a1
        JOIN a1.member m
        LEFT JOIN AnswerEntity a2 ON a2.applys = a1
        WHERE a1.exam.examSeq = (
            SELECT r.exam.examSeq FROM RoomEntity r WHERE r.roomSeq = :roomSeq
        )
        GROUP BY m.userSeq, m.nickname
        """)
        List<ApplysConfirmStatusDTO> findApplysStatusByRoom(@Param("roomSeq") Long roomSeq);

        List<ApplysEntity> findByMemberUserSeqOrderByRegdateAsc(Long userSeq); // 오래된 순
        List<ApplysEntity> findByMemberUserSeqOrderByRegdateDesc(Long userSeq); // 최신 순

        // 응시 답안 제출 -> 저장 -> 채점
        @Query("""
            SELECT a FROM ApplysEntity a
            WHERE a.member.userSeq = :userSeq
              AND a.room.roomSeq = :roomSeq
        """)
        Optional<ApplysEntity> findByUserSeqAndRoomSeq(@Param("userSeq") Long userSeq, @Param("roomSeq") Long roomSeq);





}

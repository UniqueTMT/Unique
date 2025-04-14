package com.unique.repository.exam;

import com.unique.entity.room.RoomEntity;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamParticipationRepository extends JpaRepository<RoomEntity, Long> {

  // 방 정보 + 생성자 닉네임 + 시험지까지 fetch
  @EntityGraph(attributePaths = {
      "member",
      "exam",
      "exam.quizList"
  })
  @Query("SELECT r FROM RoomEntity r WHERE r.roomSeq = :roomSeq")
  RoomEntity findRoomDetailWithExamAndMember(Long roomSeq);

  @Query("SELECT DISTINCT m.userid " +
      "FROM ApplysEntity a " +
      "JOIN a.member m " +
      "WHERE a.exam.examSeq = :examSeq")
  List<Long> findApplicantUserIdsByExam_ExamSeq(Long examSeq);

}

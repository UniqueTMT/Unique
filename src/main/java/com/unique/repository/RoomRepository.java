package com.unique.repository;

import com.unique.entity.RoomEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

  @EntityGraph(attributePaths = {
      "exam",
      "exam.member",
      "exam.quizList"
  })
  @Query("SELECT r FROM RoomEntity r WHERE r.roomSeq = :roomSeq")
  RoomEntity findRoomWithExamAndMemberAndQuizList(Long roomSeq);
}

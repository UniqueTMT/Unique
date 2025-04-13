package com.unique.repository;

import com.unique.entity.RoomEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    // 시험 방 관리
    @EntityGraph(attributePaths = {
            "exam"
    })
    @Query("SELECT r FROM RoomEntity r")
    List<RoomEntity> findRoomWithExam();

  
  @EntityGraph(attributePaths = {
      "exam",
      "exam.member",
      "exam.quizList"
  })
  @Query("SELECT r FROM RoomEntity r WHERE r.roomSeq = :roomSeq")
  RoomEntity findRoomWithExamAndMemberAndQuizList(Long roomSeq);

}

package com.unique.repository.room;

import com.unique.entity.room.RoomEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

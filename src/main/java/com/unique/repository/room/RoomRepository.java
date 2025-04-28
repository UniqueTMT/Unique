package com.unique.repository.room;

import com.unique.entity.exam.ExamEntity;
import com.unique.entity.room.RoomEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    // 열려있는 시험 조회
    @EntityGraph(attributePaths = {
        "exam",
        "exam.member"
    })
    @Query("SELECT r FROM RoomEntity r WHERE r.shutdownYn = 'N'")
    List<RoomEntity> findAllActiveRooms();


  //시험 방 관리 - 정렬
    List<RoomEntity> findAllByOrderByRegdateAsc();
    List<RoomEntity> findAllByOrderByRegdateDesc();


    @EntityGraph(attributePaths = {
      "exam",
      "exam.member",
      "exam.quizList"
  })
  @Query("SELECT r FROM RoomEntity r WHERE r.roomSeq = :roomSeq")
  RoomEntity findRoomWithExamAndMemberAndQuizList(Long roomSeq);

}

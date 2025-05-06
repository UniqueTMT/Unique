package com.unique.repository.room;

import com.unique.dto.room.MyRoomStatusDTO;
import com.unique.entity.exam.ExamEntity;
import com.unique.entity.room.RoomEntity;
import io.lettuce.core.dynamic.annotation.Param;
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

  // 시험 방 관리
  @EntityGraph(attributePaths = {
      "exam"
  })
  @Query("SELECT r FROM RoomEntity r")
  List<RoomEntity> findRoomWithExam();

  // 특정 유저 방 생성 시 사용했던 시험지 조회
  @Query("""
    SELECT new com.unique.dto.room.MyRoomStatusDTO(
        r.roomSeq,
        r.roomName,
        e.subjectName,
        r.limitTime,
        COUNT(a),
        CASE WHEN COUNT(ans) > 0 AND SUM(CASE WHEN ans.confirmed = false THEN 1 ELSE 0 END) = 0 THEN true ELSE false END
    )
    FROM RoomEntity r
    JOIN r.exam e
    LEFT JOIN ApplysEntity a ON a.exam = e
    LEFT JOIN AnswerEntity ans ON ans.applys = a
    WHERE r.member.userSeq = :userSeq
    GROUP BY r.roomSeq, r.roomName, e.subjectName, r.limitTime
    """)
  List<MyRoomStatusDTO> findRoomStatusByUser(@Param("userSeq") Long userSeq);


}

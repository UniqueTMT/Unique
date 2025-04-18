package com.unique.service.room;
import com.unique.dto.member.MemberExamHistoryDTO;
import com.unique.dto.room.RoomDTO;
import com.unique.entity.room.RoomEntity;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    Optional<RoomEntity> svcRoomDetail(Long id);
    Long svcRoomInsert(RoomDTO roomDTO, Long userSeq);
    void svcRoomUpdate(RoomEntity entity);
    void svcRoomDelete(Long id);
     //시험 방 관리
     List<RoomDTO> findRoomWithExams();

     // 시험방 남은시간 알림 기능
     Long getRemainingTime(Long roomSeq);

    //시험 방 관리 - 정렬
    List<RoomEntity> svcGetRoomsByOrder(String sort);
}

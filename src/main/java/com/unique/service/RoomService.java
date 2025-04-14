package com.unique.service;
import com.unique.dto.RoomDTO;
import com.unique.entity.AnswerEntity;
import com.unique.entity.RoomEntity;

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
}

package com.unique.service;
import com.unique.dto.RoomDTO;
import com.unique.entity.AnswerEntity;
import com.unique.entity.RoomEntity;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    List<RoomEntity> svcRoomList();
    Optional<RoomEntity> svcRoomDetail(Long id);
    void svcRoomInsert(RoomEntity entity);
    void svcRoomUpdate(RoomEntity entity);
    void svcRoomDelete(Long id);

    //시험 방 관리
//     public List<RoomDTO> findRoomWithExams();
}

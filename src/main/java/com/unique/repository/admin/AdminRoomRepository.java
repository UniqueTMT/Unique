package com.unique.repository.admin;

import com.unique.dto.admin.AdminRoomDTO;
import com.unique.entity.room.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AdminRoomRepository extends JpaRepository<RoomEntity, Long> {

    /**
     * [2-1] 현재 진행 중인 시험방 수
     */
    @Query("SELECT COUNT(r) FROM RoomEntity r WHERE r.roomStatus = '진행중'")
    Long getRunningRoomCount();

    /**
     * [2-2] 시험방 목록
     */
    @Query("SELECT new com.unique.dto.admin.AdminRoomDTO(r.roomName, r.roomStatus, r.regdate, r.shutdownYn) " +
            "FROM RoomEntity r")
    List<AdminRoomDTO> getRoomList();
}

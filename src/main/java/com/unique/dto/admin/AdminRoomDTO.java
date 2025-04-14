package com.unique.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * [DTO] 시험방 목록 정보 DTO
 * - roomName: 시험방 이름
 * - roomStatus: 시험방 상태 (예: 진행중, 종료 등)
 * - regDate: 생성일
 * - shutdownYn: 시험방 강제 종료 여부 (Y/N)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRoomDTO {
    private String roomName;
    private String roomStatus;
    private Date regDate;
    private String shutdownYn;
}

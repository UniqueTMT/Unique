package com.unique.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [DTO] 현재 진행 중인 시험방 수 DTO
 * - roomCount: 진행 중 시험방 수
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRoomCountDTO {
    private Long roomCount;
}

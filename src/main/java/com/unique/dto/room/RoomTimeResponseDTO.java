package com.unique.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTimeResponseDTO {

  private Long remainingTime;
  private String status; // 진행중, 진행전, 진행완료
  private String message; // 부가 설명 메시지

}


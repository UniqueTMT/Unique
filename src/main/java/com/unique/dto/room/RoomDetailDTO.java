package com.unique.dto.room;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailDTO {
  private String roomName;
  private Date startTime;
  private Integer limitTime;
  private Date expiredAt; // 추가
}

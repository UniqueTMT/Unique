package com.unique.dto.room;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenRoomDTO {
  private Long roomSeq;
  private String roomName;
  private String creatorNickname;
  private Integer limitCnt;
  private String hasPassword;
  private String password;
  private String category;
  private Integer limitTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date startTime;
}
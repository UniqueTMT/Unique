package com.unique.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyRoomStatusDTO {
  private Long roomSeq;
  private String roomName;
  private String category;          // Exam.subjectName
  private Integer limitTime;
  private Long applyCount;          // 응시자 수
  private Boolean confirmed;        // 모든 응시자의 모든 answer가 confirmed = true 인 경우만 true
}


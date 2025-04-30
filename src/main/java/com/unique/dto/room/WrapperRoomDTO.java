package com.unique.dto.room;

import com.unique.dto.exam.ExamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WrapperRoomDTO {
  Data data;

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
      private RoomDTO roomCreateMap;
      private ExamDTO exam;
    }
}

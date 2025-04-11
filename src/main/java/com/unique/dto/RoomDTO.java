package com.unique.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private Long roomSeq;
    private String roomName;
    private Integer limitTime;
    private Long userSeq;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    //시험 방관리
    private List<ExamDTO> examList;
}
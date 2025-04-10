package com.unique.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private Long roomSeq;
    private String title;
    private String comments;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;
}

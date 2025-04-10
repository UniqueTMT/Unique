package com.unique.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppealDTO {
    private Long appealSeq;
    private Long answerSeq;
    private String reason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;
}

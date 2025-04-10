package com.unique.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {
    private Long answerSeq;
    private Long applysSeq;
    private Long quizSeq;
    private String userAnswer;
    private Character answerYn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;
}

package com.unique.dto.answer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDTO {
    private Long answerSeq;
    private Long applysSeq;
    private Long quizSeq;
    private String userAnswer;
    private Character answerYn;
    private Long roomSeq; // Redis 접근용

    private Long userid;          // from member
    private String nickname;      // from member
    private Integer correctScore; // from quiz

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;
}

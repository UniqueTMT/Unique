package com.unique.dto.quiz;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.*;
import java.util.Date;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {
    private Long quizSeq;
    private Long examSeq;
    private String quiz;
    private Character objYn;
    private String obj1;
    private String obj2;
    private String obj3;
    private String obj4;
    private Integer correctScore;
    private String correctAnswer;
    private String hint;
    private String comments;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;
}

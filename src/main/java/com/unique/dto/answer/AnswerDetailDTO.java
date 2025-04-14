package com.unique.dto.answer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDetailDTO {
    //임의의 학생 시험 결과 확인
    private String userid;
    private String username;
    private Long quizSeq;
    private Long examSeq;
    private String quiz;
    private String objYn;
    private String obj1;
    private String obj2;
    private String obj3;
    private String obj4;
    private Integer correctScore;
    private String correctAnswer;
    private String hint;
    private String comments;

    private Long answerSeq;
    private Long applysSeq;
    private String userAnswer;
    private String answerYn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

}

package com.unique.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamResultDTO {
    private Long userid;
    private String username;
    private Long quizSeq;
    private Long examSeq;
    private String quiz;
    private String objYn;
    private String obj1;
    private String obj2;
    private String obj3;
    private String obj4;
    private Long correctScore;
    private String correctAnswer;
    private String hint;
    private String comments;
    private Long answerSeq;
    private Long applysSeq;
    private String userAnswer;
    private String answerYn;
}

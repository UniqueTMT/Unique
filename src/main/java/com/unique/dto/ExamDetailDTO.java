package com.unique.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamDetailDTO {
    //문제은행 카테고리별 문제 상세 보기
    private String subjectName;
    private String subjectCode;
    private String examCnt;
    private String examSeq;
    private String quiz;
    private String objYn;
    private String obj1;
    private String obj2;
    private String obj3;
    private String obj4;
    private String correctScore;
    private String correctAnswer;
    private String hint;
    private String comments;

}

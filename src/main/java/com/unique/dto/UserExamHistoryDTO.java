package com.unique.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExamHistoryDTO {
    //유저 시험이력 리스트 - 경준
    private Long examSeq; // 시험 시퀀스
    private Long subjectCode; // 과목 코드
    private String subjectName; // 과목명
    private String examTitle; // 시험명
    private Long userSeq; // 유저 시퀀스
    private String creatorName; // 출제자명
    private Long userid; // 응시자 아이디
    private String userName; // 응시자명
    private Long applysSeq; // 응시 시퀀스
    private Integer totalScore; // 총 배점
    private Integer obtainedScore; // 획득 총점


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate; //응시일자

}

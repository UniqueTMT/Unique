package com.unique.dto.appeal;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppealDTO {
    private Long appealSeq; // 이의제기 시퀀스
    private String contents; // 이의제기 내용
    private String appealTitle; // 이의제기 제목
    private Long applysSeq; // 응시정보 시퀀스
    private Long examSeq; // 시험정보 시퀀스
    private Long subjectSeq; // 과목 시퀀스
    private Long subjectName; // 과목 이름
    private String examTitle; // 시험 제목
    private Long userSeq; // 유저 시퀀스
    private String username; // 유저명

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;
}

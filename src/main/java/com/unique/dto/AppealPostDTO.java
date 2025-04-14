package com.unique.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//이의제기 생성 DTO 입니다. - 경준
public class AppealPostDTO {
    private String contents;         // 이의제기 내용 (필수)
    private String appealTitle;      // 이의제기 제목 (필수)
    private Long applysSeq;          // 응시 기록 시퀀스 (필수)

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;            // 등록일자 (자동 생성)
}
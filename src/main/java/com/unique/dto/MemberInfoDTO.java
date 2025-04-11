package com.unique.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoDTO {

    private Long userSeq; //유저 시퀀스
    private Long userid; // 유저 학번 (아이디)
    private String username; // 유저 이름
    private String nickname; // 유저 닉네임


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate; //응시일자

}

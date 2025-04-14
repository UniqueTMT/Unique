package com.unique.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * [DTO] 회원 접속 로그 정보 DTO
 * - userid: 사용자 학번
 * - logActive: 접속 로그 내용 (예: 로그인, 로그아웃 등)
 * - regDate: 로그 발생 시점
 * - userIp: 접속한 IP 주소
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserLogDTO {
    private Long userid;
    private String logActive;
    private Date regDate;
    private String userIp;
}

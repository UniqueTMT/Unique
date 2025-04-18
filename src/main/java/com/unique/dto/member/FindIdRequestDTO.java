package com.unique.dto.member;

import lombok.Data;

/**
 * [FindIdRequestDTO]
 * - 설명 : 사용자 이름(username)과 이메일(email)을 통해 아이디(userId)를 찾기 위한 요청 DTO
 * - 사용처 : MemberRestController의 ctlFindUserId 메서드에서 요청 데이터 전달용으로 사용
 */
@Data
public class FindIdRequestDTO {
    private String username;
    private String email;
}

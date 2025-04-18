package com.unique.dto.member;

import lombok.Data;

/**
 * [FindPwRequestDTO]
 * <p>
 * 설명 : 비밀번호 찾기를 위한 요청 DTO 클래스
 *         사용자로부터 학번(userId), 이름(username) 및 이메일(email)을 입력받아
 *         해당 회원 정보를 검증 후 비밀번호 재설정 처리를 위한 데이터 전송 객체.
 * </p>
 * <p>
 * 사용처 : MemberRestController의 ctlFindPassword 메서드에서 요청 데이터 전달에 사용됨.
 * </p>
 */
@Data
public class FindPwRequestDTO {

    private Long userId;
    private String username;
    private String email;
}

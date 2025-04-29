package com.unique.dto.member;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * eXBuilder6 JSON 요청 최상위 구조를 매핑하기 위한 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestWrapper {

    /**
     * "data" 필드를 매핑
     */
    private DataNode data;

    /**
     * data 안의 dmLogin 을 매핑
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataNode {
        private LoginRequestDTO dmLogin;
    }
}

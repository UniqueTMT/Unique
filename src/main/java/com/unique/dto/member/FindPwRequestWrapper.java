package com.unique.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * eXBuilder6 JSON 요청 최상위 구조 매핑용 래퍼
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindPwRequestWrapper {

    /** { "data": { "dmFindPw": { … } } } */
    private DataNode data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataNode {
        private FindPwRequestDTO dmFindPw;
    }
}

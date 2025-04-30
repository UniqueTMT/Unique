package com.unique.dto.member;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindIdRequestWrapper {
    private DataNode data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataNode {
        private FindIdRequestDTO dmFindId;
    }
}

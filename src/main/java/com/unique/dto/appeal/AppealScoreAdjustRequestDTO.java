package com.unique.dto.appeal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/*
 * DTO : 성적 수정 요청 DTO
 * author : 차경준
 * regdate : 25.04.15
 * */
public class AppealScoreAdjustRequestDTO {
    private Long quizSeq;       // 문제 식별자
    private int adjustedScore;  // 조정할 점수
}

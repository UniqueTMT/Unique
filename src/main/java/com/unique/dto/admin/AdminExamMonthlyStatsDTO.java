package com.unique.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [DTO] 월별 시험 응시 추이 통계를 위한 DTO
 * - subjectName: 과목명
 * - subjectCode: 과목 코드
 * - examSeq: 시험 ID
 * - month: 응시한 월 (예: 2025-04)
 * - applyCount: 응시 횟수
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminExamMonthlyStatsDTO {
    private String subjectName;
    private Long subjectCode;
    private Long examSeq;
    private String month;
    private Long applyCount;
}

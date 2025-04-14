package com.unique.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [DTO] 과목별 전체 시험 수 통계를 위한 DTO
 * - subjectName: 과목명
 * - subjectCode: 과목 코드
 * - examCount: 해당 과목 시험 수
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminExamStatsDTO {
    private String subjectName;
    private Long subjectCode;
    private Long examCount;
}

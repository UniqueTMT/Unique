package com.unique.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [DTO] 카테고리별 월 기준 시험 응시 추이 통계용 DTO
 * - subjectName: 과목명
 * - subjectCode: 과목코드
 * - month: 응시월 (yyyy-MM 형식)
 * - count: 응시자 수
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminExamMonthDTO {
    private String subjectName;
    private Long subjectCode;
    private String month;
    private Long examCount;
}

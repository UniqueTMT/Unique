package com.unique.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * [DTO] 최근 생성된 시험 정보 DTO
 * - subjectName: 과목명
 * - regdate: 시험 등록일
 * - examTitle: 시험 제목
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRecentExamDTO {
    private String subjectName;
    private Date regdate;
    private String examTitle;
}

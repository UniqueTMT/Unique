package com.unique.dto.answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentExamResultDTO {
    private String subjectName;
    private String roomName;
    private Integer professorScore;
    private Long userid;
    private String username;
}

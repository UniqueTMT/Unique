package com.unique.dto.exam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryQuizCountDTO {
    private String subjectName;
    private Long subjectCode;
    private Long quizCount;
}

package com.unique.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamDTO {
    private Long examSeq;
    private String examTitle;
    private String comments;
    private Long subjectCode;
    private String subjectName;
    private Long userSeq;
    private Integer examCnt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    private List<QuizDTO> quizList;
}

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
    private String title;
    private String comments;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    //문제은행 카테고리별 문제 상세 보기
    private List<QuizDTO> quizList;
}

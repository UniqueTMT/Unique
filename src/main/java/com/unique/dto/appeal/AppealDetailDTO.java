package com.unique.dto.appeal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.unique.dto.answer.AnswerDTO;
import com.unique.dto.exam.ExamDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/*
* DTO : 이의제기 세부 정보 DTO
* author : 차경준
* regdate : 25.04.15
* */
public class AppealDetailDTO {
    private AppealDTO appeal;          // 기존 AppealDTO
    private ExamDTO exam;              // 시험 정보 (ExamDTO)
    private List<AnswerDTO> answers;   // 학생 답안 목록 (AnswerDTO)
    private int currentTotalScore;     // 현재 총점
    private int currentCorrectCount;   // 현재 맞은 개수
    private int currentWrongCount;     // 현재 틀린 개수

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;
}

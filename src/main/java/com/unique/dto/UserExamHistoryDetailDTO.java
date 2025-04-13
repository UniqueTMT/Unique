package com.unique.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 유저가 응시한 특정 시험의 세부 정보를 담는 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExamHistoryDetailDTO {
    
    // 시험 이력 세부 DTO (시험지 정보) - 경준
    private Long examSeq;          // 시험 시퀀스
    private Long subjectCode;      // 과목 코드
    private String subjectName;    // 과목명
    private String examTitle;      // 시험명
    private Long userSeq;          // 유저 시퀀스
    private String creatorName;    // 출제자명
    private Long userid;           // 응시자 아이디
    private String userName;       // 응시자명
    private Long applysSeq;        // 응시 시퀀스
    private Integer totalScore;    // 총 배점
    private Integer obtainedScore; // 획득 점수

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;          // 응시일자

    private List<QuizDTO> quizList;        // 시험 문제 리스트
    private List<AnswerDTO> answerList;    // 사용자 답변 리스트
}

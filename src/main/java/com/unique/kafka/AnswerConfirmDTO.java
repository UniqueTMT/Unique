package com.unique.kafka;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerConfirmDTO {
  
  // 아래의 컬럼에 2차 채점 내용을 담아 /api/answer로 전달 -> 2차 채점 제출 버튼 기능이라고 보면 됨
  
  private Long answerSeq;   // 정답 번호
  private Integer professorScore;   // 2차 교수 점수
  private String professorFeedback; // 2차 교수 피드백
}

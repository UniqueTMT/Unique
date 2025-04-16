package com.unique.kafka;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerKafkaDTO {
  private Long answerSeq;     // 답안 고유 ID
  private Long applysSeq;     // 응시자 정보
  private Long quizSeq;       // 문제 ID
  private Long roomSeq;       // 시험방 ID
  private String userAnswer;  // 사용자의 제출 답안
}

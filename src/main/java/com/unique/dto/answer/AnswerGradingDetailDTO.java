package com.unique.dto.answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerGradingDetailDTO {
  private Long answerSeq;
  private String quiz;
  private String userAnswer;
  private Integer aiScore;
  private String aiFeedback;
  private Boolean confirmed;
  private Integer professorScore;
  private String professorFeedback;
}

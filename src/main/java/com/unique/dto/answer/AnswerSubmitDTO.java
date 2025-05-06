package com.unique.dto.answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerSubmitDTO {
  private Long userSeq;
  private Long roomSeq;
  private List<AnswerDTO> answers;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class AnswerDTO {
    private Long quizSeq;
    private String answer;
  }
}

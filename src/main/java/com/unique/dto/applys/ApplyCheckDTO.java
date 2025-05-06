package com.unique.dto.applys;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyCheckDTO {

  private Long answerSeq;
  private Integer professorScore;
  private String professorFeedback;

}
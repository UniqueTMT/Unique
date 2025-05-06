package com.unique.dto.applys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplysConfirmStatusDTO {
  private Long userSeq;
  private String nickname;
  private Boolean confirmed;
}


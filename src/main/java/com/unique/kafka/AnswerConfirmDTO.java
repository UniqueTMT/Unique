package com.unique.kafka;

import com.unique.dto.applys.ApplyCheckDTO;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerConfirmDTO {
  
  // 아래의 컬럼에 2차 채점 내용을 담아 /api/answer로 전달 -> 2차 채점 제출 버튼 기능이라고 보면 됨
  private List<ApplyCheckDTO> applyCheckList;

}

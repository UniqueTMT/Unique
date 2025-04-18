package com.unique.controller.exam;

import com.unique.dto.exam.ExamParticipationDTO;
import com.unique.service.exam.ExamParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exam-participation")
@RequiredArgsConstructor
public class ExamParticipationController {

  private final ExamParticipationService examParticipationService;

  // 시험응시 : 시험방 리스트 클릭 -> 입장
  @GetMapping("/{roomSeq}")
  public ExamParticipationDTO getExamParticipationDetail(@PathVariable Long roomSeq) {
    return examParticipationService.getExamParticipationDetail(roomSeq);
  }
}

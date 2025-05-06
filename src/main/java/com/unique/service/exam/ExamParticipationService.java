package com.unique.service.exam;

import com.unique.dto.exam.ExamParticipationDTO;

public interface ExamParticipationService {
  ExamParticipationDTO getExamParticipationDetail(Long roomSeq, Long userSeq);
}

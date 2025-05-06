package com.unique.service.answer;

import com.unique.dto.answer.AnswerDTO;
import com.unique.dto.answer.AnswerDetailDTO;
import com.unique.dto.answer.AnswerGradingDetailDTO;
import com.unique.dto.answer.AnswerSubmitDTO;
import com.unique.dto.answer.StudentExamResultDTO;
import com.unique.entity.answer.AnswerEntity;

import com.unique.kafka.AnswerConfirmDTO;
import java.util.List;
import java.util.Optional;

public interface AnswerService {
    List<AnswerEntity> svcGetAllAnswers();
    Optional<AnswerEntity> svcGetAnswer(Long userid);
    void svcCreateAnswer(AnswerEntity answer);
    void svcDeleteAnswer(Long userid);

    //응시자 답안 확인
    List<AnswerDTO> svcGetAllMembersAnswers();

    //
    List<StudentExamResultDTO> svcFindStudentExamResultsByUserid(Long userid);
    List<AnswerDetailDTO> svcFindSelectedStudentResult(Long userid);

    // 답안 제출 및 채점
    void saveAllAnswers(AnswerSubmitDTO submitDTO, Long applysSeq);
    void saveOrUpdateAnswer(AnswerDTO dto);

    // 2차 채점 로직
    void confirmGrading(AnswerConfirmDTO dto); // (✔ dto 내부에 List<ApplyCheckDTO>)

    // 응시자 1차 채점 확인
    List<AnswerGradingDetailDTO> getGradingDetail(Long userSeq, Long roomSeq);

}
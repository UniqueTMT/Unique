package com.unique.service.answer;

import com.unique.dto.answer.AnswerDTO;
import com.unique.dto.answer.AnswerDetailDTO;
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

    //임의의 학생 시험 결과 확인
    List<StudentExamResultDTO> svcFindStudentExamResultsByUserid(Long userid);
    List<AnswerDetailDTO> svcFindSelectedStudentResult(Long userid);

    // 응시 답안 제출 -> 저장 -> 채점
    void saveOrUpdateAnswer(AnswerDTO answerDTO);

    // 2차 채점 로직
    void confirmGrading(AnswerConfirmDTO dto);
}
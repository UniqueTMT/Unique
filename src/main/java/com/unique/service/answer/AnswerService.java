package com.unique.service.answer;

import com.unique.dto.answer.AnswerDTO;
import com.unique.dto.answer.AnswerDetailDTO;
import com.unique.entity.answer.AnswerEntity;

import java.util.List;
import java.util.Optional;

public interface AnswerService {
    List<AnswerEntity> svcGetAllAnswers();
    Optional<AnswerEntity> svcGetAnswer(Long userid);
    void svcCreateAnswer(AnswerEntity answer);
    void svcUpdateAnswer(AnswerEntity answer);
    void svcDeleteAnswer(Long userid);

    //응시자 답안 확인
    List<AnswerDTO> svcGetAllMembersAnswers();

    //임의의 학생 시험 결과 확인
    List<AnswerDetailDTO> svcFindSelectedStudentResult(Long userid);

}
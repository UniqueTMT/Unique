package com.unique.service;

import com.unique.dto.AnswerDTO;
import com.unique.dto.AnswerDetailDTO;
import com.unique.entity.AnswerEntity;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// AnswerService.java
public interface AnswerService {
    List<AnswerEntity> svcGetAllAnswers();
    Optional<AnswerEntity> svcGetAnswer(Long userid);
    void svcCreateAnswer(AnswerEntity answer);
    void svcUpdateAnswer(AnswerEntity answer);
    void svcDeleteAnswer(Long userid);

    //응시자 답안 확인
    List<AnswerDTO> svcFindAnswerWithMemberAndQuiz();

    //임의의 학생 시험 결과 확인
    List<AnswerDetailDTO> svcFindSelectedStudentResult(Long userid);

}
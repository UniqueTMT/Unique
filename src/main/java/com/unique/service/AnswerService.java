package com.unique.service;

import com.unique.dto.TestDTO;
import com.unique.entity.AnswerEntity;

import java.util.List;
import java.util.Optional;

// AnswerService.java
public interface AnswerService {
    List<AnswerEntity> svcGetAllAnswers();
    Optional<AnswerEntity> svcGetAnswer(Long id);
    void svcCreateAnswer(AnswerEntity answer);
    void svcUpdateAnswer(AnswerEntity answer);
    void svcDeleteAnswer(Long id);

    //사용자 정의 추가 기능
//    public List<TestDTO> svcTest();

}
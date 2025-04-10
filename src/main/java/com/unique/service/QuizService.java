package com.unique.service;

import com.unique.entity.QuizEntity;

import java.util.List;
import java.util.Optional;
public interface QuizService {
    List<QuizEntity> svcQuizList();
    Optional<QuizEntity> svcQuizDetail(Long id);
    void svcQuizInsert(QuizEntity entity);
    void svcQuizUpdate(QuizEntity entity);
    void svcQuizDelete(Long id);
}

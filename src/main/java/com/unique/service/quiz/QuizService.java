package com.unique.service.quiz;

import com.unique.dto.quiz.QuizDTO;
import com.unique.entity.quiz.QuizEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
public interface QuizService {
    Optional<QuizEntity> svcQuizDetail(Long id);
    List<Map<String, Object>> svcGetQuizListMap(Long examSeq);
    void svcQuizInsert(QuizEntity entity);
    void svcUpdateQuizMap(Long quizSeq, Map<String, Object> quizData);
    void svcDeleteQuiz(Long quizSeq);
    void svcSaveAllQuiz(List<QuizDTO> quizList);
    void svcPublishExam(Long examSeq);
}

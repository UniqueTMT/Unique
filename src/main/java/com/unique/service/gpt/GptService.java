package com.unique.service.gpt;

import com.unique.entity.exam.ExamEntity;
import com.unique.entity.quiz.QuizEntity;

import java.util.List;

public interface GptService {
    String svcCallGpt(String text, String prompt);
    List<QuizEntity> svcParseGptResponse(String json, ExamEntity exam) throws Exception;
}

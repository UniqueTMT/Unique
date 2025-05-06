package com.unique.service.gpt;

import com.unique.dto.gpt.GPTScoreResult;
import com.unique.entity.exam.ExamEntity;
import com.unique.entity.quiz.QuizEntity;

import java.util.List;

public interface GptService {
    String svcCallGpt(String text, String prompt);
    List<QuizEntity> svcParseGptResponse(String json, ExamEntity exam) throws Exception;

    // 주관식 GPT 채점 메서드
    GPTScoreResult gradeAnswer(String userAnswer, String referenceAnswer, int fullScore);
}
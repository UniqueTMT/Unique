package com.unique.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unique.entity.exam.ExamEntity;
import com.unique.entity.quiz.QuizEntity;
import com.unique.repository.exam.ExamRepository;
import com.unique.repository.quiz.QuizRepository;
import com.unique.service.gpt.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GptKafkaConsumer {

    private final GptService gptService;
    private final QuizRepository quizRepository;
    private final ExamRepository examRepository;

    @KafkaListener(topics = "gpt-question-request", groupId = "gpt-group")
    public void consume(String message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = mapper.readValue(message, new TypeReference<>() {});

        Long examSeq = Long.valueOf(data.get("examSeq"));
        ExamEntity exam = examRepository.findById(examSeq)
                .orElseThrow(() -> new IllegalArgumentException("시험 정보 없음: " + examSeq));

        String gptResult = gptService.svcCallGpt(data.get("text"), data.get("prompt"));
        List<QuizEntity> quizzes = gptService.svcParseGptResponse(gptResult, exam);

        quizRepository.saveAll(quizzes);
    }

}

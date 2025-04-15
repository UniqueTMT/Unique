package com.unique.impl.gpt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unique.client.GptClient;
import com.unique.entity.exam.ExamEntity;
import com.unique.entity.quiz.QuizEntity;
import com.unique.service.gpt.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GptServiceImpl implements GptService {

    private final GptClient gptClient;

    //GPT 호출 (프롬프트만 넘김)
    @Override
    public String svcCallGpt(String text, String prompt) {
        try {
            return gptClient.sendToGpt(prompt);
        } catch (IOException e) {
            throw new RuntimeException("GPT 호출 중 오류 발생", e);
        }
    }

    //GPT 응답을 QuizEntity 리스트로 변환
    @Override
    public List<QuizEntity> svcParseGptResponse(String json, ExamEntity exam) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> list = mapper.readValue(json, new TypeReference<>() {});

        List<QuizEntity> quizList = new ArrayList<>();
        Set<String> objYnTypes = new HashSet<>();

        for (Map<String, Object> data : list) {
            String objYn = (String) data.getOrDefault("objYn", "1"); // 기본값: 주관식
            objYnTypes.add(objYn);

            QuizEntity quiz = QuizEntity.builder()
                    .exam(exam)
                    .quiz((String) data.get("quiz"))
                    .objYn(objYn)
                    .obj1((String) data.get("obj1"))
                    .obj2((String) data.get("obj2"))
                    .obj3((String) data.get("obj3"))
                    .obj4((String) data.get("obj4"))
                    .correctAnswer((String) data.get("correctAnswer"))
                    .correctScore(parseIntSafe(data.get("correctScore"), 10))
                    .hint((String) data.get("hint"))
                    .comments((String) data.get("comments"))
                    .regdate(new Date())
                    .build();
            quizList.add(quiz);
        }
        return quizList;
    }

    private Integer parseIntSafe(Object value, int defaultVal) {
        try {
            if (value instanceof Integer) return (Integer) value;
            if (value instanceof String) return Integer.parseInt((String) value);
        } catch (Exception ignored) {}
        return defaultVal;
    }
}

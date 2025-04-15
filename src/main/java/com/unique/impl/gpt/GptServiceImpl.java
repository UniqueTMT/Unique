package com.unique.impl.gpt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unique.entity.exam.ExamEntity;
import com.unique.entity.quiz.QuizEntity;
import com.unique.service.gpt.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GptServiceImpl implements GptService {

    private final GptClient gptClient;

    @Override
    public String svcCallGpt(String text, String prompt) {
        return gptClient.sendToGpt(text, prompt);
    }

    @Override
    public List<QuizEntity> svcParseGptResponse(String json, ExamEntity exam) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> list = mapper.readValue(json, new TypeReference<>() {});
        return list.stream().map(data -> QuizEntity.builder()
                .exam(exam)
                .quiz((String) data.get("quiz"))
                .objYn((String) data.getOrDefault("objYn", "0"))
                .obj1((String) data.get("obj1"))
                .obj2((String) data.get("obj2"))
                .obj3((String) data.get("obj3"))
                .obj4((String) data.get("obj4"))
                .correctAnswer((String) data.get("correctAnswer"))
                .correctScore((Integer) data.getOrDefault("correctScore", 10))
                .hint((String) data.get("hint"))
                .comments((String) data.get("comments"))
                .regdate(new Date())
                .build()).toList();
    }
}

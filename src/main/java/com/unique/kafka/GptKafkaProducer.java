package com.unique.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GptKafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendQuestionRequest(String text, String prompt, Long examSeq) {
        try {
            String json = objectMapper.writeValueAsString(Map.of("text", text, "prompt", prompt, "examSeq", examSeq));
            kafkaTemplate.send("gpt-question-request", json);
        } catch (Exception e) {
            throw new RuntimeException("Kafka 전송 실패", e);
        }
    }
}

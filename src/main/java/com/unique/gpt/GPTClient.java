package com.unique.gpt;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class GPTClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey = "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";

    public String sendPrompt(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4",
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", 0.7,
                "max_tokens", 1000
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions", entity, String.class
        );

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.getBody());
            String content = json.get("choices").get(0).get("message").get("content").asText();

            System.out.println("✅ GPT 응답 본문: \n" + content); // 디버깅 로그
            return content;

        } catch (Exception e) {
            System.err.println("❌ GPT 응답 파싱 오류: " + e.getMessage());
            throw new RuntimeException("GPT 응답 파싱 오류", e);
        }
    }
}
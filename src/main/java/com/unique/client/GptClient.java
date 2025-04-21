package com.unique.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GptClient {

//    @Value("${openai.api-key}")
    private String apiKey = "";

    private final String GPT_API_URL = "https://api.openai.com/v1/chat/completions";

    public String sendToGpt(String prompt) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4");
        requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 1000);

        StringEntity entity = new StringEntity(objectMapper.writeValueAsString(requestBody), "UTF-8");

        HttpPost post = new HttpPost(GPT_API_URL);
        post.setHeader("Authorization", "Bearer " + apiKey);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(entity);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            JsonNode json = objectMapper.readTree(responseBody);

            // ✅ 응답 구조 확인
            if (json.has("choices")) {
                JsonNode choices = json.get("choices");
                if (choices.isArray() && choices.size() > 0) {
                    return choices.get(0).get("message").get("content").asText();
                }
            }

            // ✅ 예외 응답 fallback
            if (json.has("error")) {
                return "[GPT 응답 오류] " + json.get("error").get("message").asText();
            }

            return "[GPT 응답 없음]";
        }
    }

}

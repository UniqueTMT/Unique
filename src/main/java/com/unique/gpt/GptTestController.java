//package com.unique.gpt;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/gptTest")
//public class GptTestController {
//
//    //============================================코드 테스트용 입니다=========================================
//    private final String OPENAI_API_KEY = "sk-proj-/";
//    private final String GPT_API_URL = "https://api.openai.com/v1/chat/completions";
//
//    @GetMapping(value = "/test", produces = MediaType.TEXT_PLAIN_VALUE)
//    public ResponseEntity<String> testGptCall() {
//        try {
//            String hardcodedPrompt = """
//                    다음 자바 문제를 만들어줘.
//
//                    - 주제: 변수 선언
//                    - 유형: 객관식
//                    - 개수: 3문제
//                    - 형식: 보기 4개 (A~D) + 정답 + 해설
//                    """;
//
//            String gptResponse = callGptApi(hardcodedPrompt);
//            return ResponseEntity.ok(gptResponse);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("오류: " + e.getMessage());
//        }
//    }
//
//    private String callGptApi(String prompt) throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("model", "gpt-3.5-turbo");
//        requestBody.put("messages", List.of(
//                Map.of("role", "user", "content", prompt)
//        ));
//        requestBody.put("temperature", 0.7);
//        requestBody.put("max_tokens", 1000);
//
//        StringEntity entity = new StringEntity(objectMapper.writeValueAsString(requestBody), "UTF-8");
//        entity.setContentType("application/json");
//
//        HttpPost post = new HttpPost(GPT_API_URL);
//        post.setHeader("Authorization", "Bearer " + OPENAI_API_KEY);
//        post.setHeader("Content-Type", "application/json");
//        post.setEntity(entity);
//
//        try (CloseableHttpClient httpClient = HttpClients.createDefault();
//             CloseableHttpResponse response = httpClient.execute(post)) {
//
//            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
//            JsonNode json = objectMapper.readTree(responseBody);
//
//            return json.get("choices").get(0).get("message").get("content").asText();
//        }
//    }
//}

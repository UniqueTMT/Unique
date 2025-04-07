package com.example.unique.gpt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gpt")
public class GptQuestionController {

    private final String OPENAI_API_KEY = "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"; // 실제 키로 교체하세요
    private final String GPT_API_URL = "https://api.openai.com/v1/chat/completions";

    private final int MAX_INPUT_TOKENS = 7000; // gpt-4 기준
    private final int APPROX_CHARS_PER_TOKEN = 2;

    @PostMapping(value = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> generateQuestionsWithTemplate(
            @RequestPart("pdf") MultipartFile pdfFile,
            @RequestParam("category") String category,
            @RequestParam("chapter") String chapter,
            @RequestParam("type") String type,
            @RequestParam("count") String count,
            @RequestParam("prompt") String userPrompt
    ) throws IOException {

        // PDF 텍스트 추출
        String text = extractTextFromPdf(pdfFile);

        // 토큰 길이에 맞춰 자르기
        int maxChars = MAX_INPUT_TOKENS * APPROX_CHARS_PER_TOKEN;
        if (text.length() > maxChars) {
            text = text.substring(0, maxChars);
        }

        // 질문 템플릿 프롬프트 구성
        String finalPrompt = buildPromptWithTemplate(category, chapter, type, count, userPrompt, text);

        // GPT API 호출
        String gptResponse = callGptApi(finalPrompt);

        return ResponseEntity.ok(gptResponse);
    }

    private String extractTextFromPdf(MultipartFile pdfFile) throws IOException {
        try (PDDocument document = PDDocument.load(pdfFile.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String buildPromptWithTemplate(
            String category, String chapter, String type, String count, String userPrompt, String text
    ) {
        return String.format("""
                당신은 교육 전문가입니다. 다음의 학습 자료를 기반으로 문제를 생성해주세요.

                🏷️ 카테고리: %s
                📚 챕터: %s
                📌 문제 유형: %s
                🔢 생성할 문제 수: %s
                🧠 추가 지시사항: %s

                아래는 학습 자료입니다. 이 자료를 분석하여 요청한 유형과 수량에 맞게 문제를 생성해주세요.
                가능하면 다양한 유형(객관식, 단답형, 서술형 등)을 포함하고, 문제는 각기 다른 내용을 다루도록 해주세요.

                [학습자료 시작]
                %s
                [학습자료 끝]
                """, category, chapter, type, count, userPrompt, text);
    }

    private String callGptApi(String prompt) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4");
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 1000); // 출력 제한

        StringEntity entity = new StringEntity(objectMapper.writeValueAsString(requestBody), "UTF-8");
        entity.setContentType("application/json");

        HttpPost post = new HttpPost(GPT_API_URL);
        post.setHeader("Authorization", "Bearer " + OPENAI_API_KEY);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(entity);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            JsonNode json = objectMapper.readTree(responseBody);
            return json.get("choices").get(0).get("message").get("content").asText();
        }
    }
}

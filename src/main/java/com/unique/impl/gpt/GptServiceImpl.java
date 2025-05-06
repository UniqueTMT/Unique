package com.unique.impl.gpt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unique.client.GptClient;
import com.unique.dto.gpt.GPTScoreResult;
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
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> list = mapper.readValue(json, new TypeReference<>() {});

            List<QuizEntity> quizList = new ArrayList<>();
            for (Map<String, Object> data : list) {
                QuizEntity quiz = QuizEntity.builder()
                        .exam(exam)
                        .quiz((String) data.get("quiz"))
                        .objYn((String) data.getOrDefault("objYn", "1"))
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

        } catch (Exception e) {
            throw new RuntimeException("GPT 응답 JSON 파싱 오류", e);
        }
    }

    private Integer parseIntSafe(Object value, int defaultVal) {
        try {
            if (value instanceof Integer) return (Integer) value;
            if (value instanceof String) return Integer.parseInt((String) value);
        } catch (Exception ignored) {}
        return defaultVal;
    }

    // GPT 채점 로직 (모의 응답 또는 API 연동 가능)
    @Override
    public GPTScoreResult gradeAnswer(String userAnswer, String referenceAnswer, int fullScore) {
        String prompt = buildGradingPrompt(userAnswer, referenceAnswer, fullScore);

        try {
            String gptResponse = gptClient.sendToGpt(prompt);

            int score = 0;
            String feedback = "";

            for (String line : gptResponse.split("\n")) {
                if (line.contains("점수")) {
                    score = Integer.parseInt(line.replaceAll("[^0-9]", ""));
                } else if (line.contains("피드백")) {
                    feedback = line.replaceFirst("피드백[:：]?", "").trim();
                }
            }

            return new GPTScoreResult(score, feedback);

        } catch (Exception e) {
            return new GPTScoreResult(0, "GPT 채점 실패: " + e.getMessage());
        }
    }


    private String buildGradingPrompt(String userAnswer, String referenceAnswer, int fullScore) {
        return """
        아래는 수험자의 답변과 정답 예시입니다. 정답 예시를 기준으로 수험자의 답변을 채점해주세요.
        만점은 %d점이며, 완전히 정답일 경우 %d점을 부여하고, 부분 정답일 경우 내용을 기반으로 적절히 감점 후 점수를 부여해주세요.

        [정답 예시]
        %s

        [수험자 답변]
        %s

        출력 형식:
        점수: <숫자>
        피드백: <간단한 평가 코멘트>
        """.formatted(fullScore, fullScore, referenceAnswer, userAnswer);
    }


}

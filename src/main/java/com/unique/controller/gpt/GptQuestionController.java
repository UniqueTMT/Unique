package com.unique.controller.gpt;

import com.unique.client.GptClient;
import com.unique.config.security.CustomUserDetails;
import com.unique.entity.exam.ExamEntity;
import com.unique.entity.member.MemberEntity;
import com.unique.entity.quiz.QuizEntity;
import com.unique.kafka.GptKafkaProducer;
import com.unique.repository.exam.ExamRepository;
import com.unique.repository.quiz.QuizRepository;
import com.unique.service.gpt.GptPromptService;
import com.unique.service.gpt.GptService;
import com.unique.service.gpt.PdfParseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gpt")
@RequiredArgsConstructor
public class GptQuestionController {

    private final GptPromptService gptPromptService;
    private final GptClient gptClient;
    private final PdfParseService pdfParseService;
    private final GptService gptService;
    private final GptKafkaProducer gptKafkaProducer;
    private final ExamRepository examRepository;
    private final QuizRepository quizRepository;

    private final int MAX_INPUT_TOKENS = 7000;
    private final int APPROX_CHARS_PER_TOKEN = 2;

    @PostMapping(value = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> ctlGenerateQuestionsWithTemplate(
            @RequestPart("pdf") MultipartFile pdfFile,
            @RequestParam("category") String category,
            @RequestParam("chapter") String chapter,
            @RequestParam("type") String type,
            @RequestParam("count") String count,
            @RequestParam("prompt") String userPrompt
    ) throws Exception {
        // 1. 로그인 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        MemberEntity member = userDetails.getMember();

        // 2. PDF 텍스트 추출
        String text = pdfParseService.svcExtractText(pdfFile);
        int maxChars = MAX_INPUT_TOKENS * APPROX_CHARS_PER_TOKEN;
        if (text.length() > maxChars) {
            text = text.substring(0, maxChars);
        }

        // 3. GPT 프롬프트 생성
        String prompt = gptPromptService.svcBuildPrompt(category, chapter, type, count, userPrompt, text);

        // 4. GPT API 호출
        String gptResponse = gptClient.sendToGpt(prompt);

        // 5. 시험 생성 (출제자 userSeq 포함)
        ExamEntity exam = examRepository.save(
                ExamEntity.builder()
                        .examTitle(userPrompt + " 기반 시험")
                        .subjectName(category)
                        .subjectCode(999L)
                        .examCnt(Integer.parseInt(count))
                        .pubYn("0")
                        .member(member)  // ✅ 로그인 사용자 ID 삽입
                        .regdate(new Date())
                        .build()
        );

        // 6. Kafka 메시지 전송
        gptKafkaProducer.sendQuestionRequest(text, prompt, exam.getExamSeq());

        // 7. GPT 응답 → 문제 파싱 및 저장
        List<QuizEntity> quizList = gptService.svcParseGptResponse(gptResponse, exam);
        quizRepository.saveAll(quizList);

        // 8. 응답 반환
//        return ResponseEntity.ok("문제 생성 및 저장 완료. 시험번호: " + exam.getExamSeq());
        // ✅ 응답 데이터 구성
        Map<String, Object> result = new HashMap<>();
        result.put("message", "문제 생성 및 저장 완료");
        result.put("examSeq", exam.getExamSeq());
        result.put("examTitle", exam.getExamTitle());
        result.put("quizList", quizList); // JSON 직렬화에 문제가 없도록 DTO 변환 권장

        return ResponseEntity.ok()
                .header("Access-Control-Expose-Headers", "Content-Disposition")
                .body(result);

    }
}
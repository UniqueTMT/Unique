package com.unique.controller.gpt;

import com.unique.client.GptClient;
import com.unique.entity.exam.ExamEntity;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

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

        // 1) PDF 텍스트 추출
        String text = pdfParseService.svcExtractText(pdfFile);

        int maxChars = MAX_INPUT_TOKENS * APPROX_CHARS_PER_TOKEN;
        if (text.length() > maxChars) {
            text = text.substring(0, maxChars);
        }

        // 2) GPT 프롬프트 생성
        String prompt = gptPromptService.svcBuildPrompt(category, chapter, type, count, userPrompt, text);

        // 3) GPT 호출
        String gptResponse = gptClient.sendToGpt(prompt);

        // 4) 시험 저장 - Consumer가 quiz에 참조
        ExamEntity exam = examRepository.save(
                ExamEntity.builder()
                        .examTitle(userPrompt + " 기반 시험")
                        .subjectName(category)
                        .subjectCode(999L)
                        .examCnt(Integer.parseInt(count))
                        .pubYn("0")
                        .regdate(new Date())
                        .build()
        );
        gptKafkaProducer.sendQuestionRequest(text, gptPromptService.svcBuildPrompt(category, chapter, type, count, userPrompt, text), exam.getExamSeq());

        // 5) GPT 응답 파싱 → 문제 리스트로 변환
        List<QuizEntity> quizList = gptService.svcParseGptResponse(gptResponse, exam);

        // 6) DB 저장
        quizRepository.saveAll(quizList);

        return ResponseEntity.ok("문제 생성 및 저장 완료. 시험번호: " + exam.getExamSeq());
    }
}
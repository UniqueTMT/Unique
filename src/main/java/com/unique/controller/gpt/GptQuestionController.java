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
    public ResponseEntity<Map<String, Object>> ctlGenerateQuestionsWithTemplate(
            @RequestPart("pdf") MultipartFile pdfFile,
            @RequestParam("category") String category,
            @RequestParam("chapter") String chapter,
            @RequestParam("type") String type,
            @RequestParam("count") String count,
            @RequestParam("prompt") String userPrompt
    ) throws Exception {
        // 1. 로그인 사용자 정보 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        MemberEntity member = userDetails.getMember();

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

        // 4) 시험 저장
        // user_seq=1, 과목코드 200
        MemberEntity member = new MemberEntity();
        member.setUserSeq(1L);

        ExamEntity exam = examRepository.save(
                ExamEntity.builder()
                        .examTitle(userPrompt + "시험")
                        .subjectName(category)
                        .subjectCode(200L)
                        .examCnt(Integer.parseInt(count))
                        .pubYn("0")
                        .regdate(new Date())
                        .member(member)
                        .build()
        );



        // 5) GPT 응답 파싱 → 문제 리스트로 변환
        List<QuizEntity> quizList = gptService.svcParseGptResponse(gptResponse, exam);

        // 6) DB 저장
        quizRepository.saveAll(quizList);

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
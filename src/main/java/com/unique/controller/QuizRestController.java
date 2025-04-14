package com.unique.controller;
import com.unique.dto.QuizDTO;
import com.unique.entity.ExamEntity;
import com.unique.entity.QuizEntity;
import com.unique.repository.ExamRepository;
import com.unique.repository.QuizRepository;
import com.unique.service.QuizService;
import com.unique.service.QuizServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizRestController {
    private final QuizService quizService;
    private final ExamRepository examRepository;
    private final QuizRepository quizRepository;

    //생성된 문제 전체 보기
    @GetMapping("/quizlist")
    public ResponseEntity<List<QuizEntity>> ctlQuizList() {
        return ResponseEntity.ok(quizService.svcQuizList());
    }

    @PostMapping("/quizlist")
    public void ctlQuizInsert(@RequestBody QuizEntity entity) {
        quizService.svcQuizInsert(entity);
    }

    //특정 문제 수정
    @PutMapping("/quizlist")
    public void ctlQuizUpdate(@RequestBody QuizEntity entity) {
        quizService.svcQuizUpdate(entity);
    }

    //특정 문제 삭제
    @DeleteMapping("/quizlist/{id}")
    public void ctlQuizDelete(@PathVariable(value="id") Long id) {
        quizService.svcQuizDelete(id);
    }

    @GetMapping("/quizlist/{id}")
    public ResponseEntity<Optional<QuizEntity>> ctlQuizDetail(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(quizService.svcQuizDetail(id));
    }

    /**
     * 문제들을 특정 시험(exam)에 연결하여 출제 처리
     */
//    @PostMapping("/publish")
//    public ResponseEntity<String> publishQuizToExam(@RequestParam Long examSeq) {
//        ExamEntity exam = examRepository.findById(examSeq)
//                .orElseThrow(() -> new RuntimeException("시험을 찾을 수 없습니다."));
//
//        List<QuizEntity> quizzes = quizRepository.findAllByExamIsNull(); // 또는 상태 필터링
//
//        for (QuizEntity quiz : quizzes) {
//            quiz.setExam(exam); // 🔗 문제 ↔ 시험 연결
//        }
//
//        quizRepository.saveAll(quizzes); // 일괄 저장
//
//        return ResponseEntity.ok("출제가 완료되었습니다.");
//    }
    @PostMapping("/publish-test")
    public ResponseEntity<String> publishQuizToExamTest() {
        // 테스트용 examSeq 하드코딩
        Long examSeq = 1L;
        ExamEntity exam = examRepository.findById(examSeq)
                .orElseThrow(() -> new RuntimeException("시험을 찾을 수 없습니다."));

        List<QuizEntity> quizzes = quizRepository.findAllByExamIsNull();

        for (QuizEntity quiz : quizzes) {
            quiz.setExam(exam); // 시험 연결
        }

        quizRepository.saveAll(quizzes);
        return ResponseEntity.ok("====하드코딩으로 출제 완료 (시험 ID: " + examSeq + ")====");
    }

    //pdf 인식
//    @PostMapping("/generate")
//    public ResponseEntity<List<QuizDTO>> generateQuiz(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("prompt") String prompt
//    ) {
//        try {
//            List<QuizDTO> generatedQuizList = quizService.generateQuizFromPdf(file, prompt);
//            return ResponseEntity.ok(generatedQuizList);
//        } catch (IOException e) {
//            return ResponseEntity.internalServerError().build();
//        }
//    }
    /**
     * GPT를 활용해 문제를 생성하고 DB에 저장함
     */
//    @PostMapping(value = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<List<QuizDTO>> generateQuizFromPdfAndPrompt(
//            @RequestPart("pdf") MultipartFile pdfFile,
//            @RequestParam("category") String category,
//            @RequestParam("chapter") String chapter,
//            @RequestParam("type") String type,
//            @RequestParam("count") String count,
//            @RequestParam("prompt") String userPrompt
//    ) {
//        try {
//            // 1. 사용자 템플릿 프롬프트 조립
//            String finalPrompt = buildPrompt(category, chapter, type, count, userPrompt, pdfFile);
//
//            // 2. 문제 생성 및 저장
//            List<QuizDTO> generatedQuizzes = quizService.generateQuizFromPdf(pdfFile, finalPrompt);
//
//            return ResponseEntity.ok(generatedQuizzes);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().build();
//        }
//    }
//
//    private String buildPrompt(
//            String category, String chapter, String type, String count, String userPrompt, MultipartFile pdfFile
//    ) throws IOException {
//        String text;
//        try (var doc = org.apache.pdfbox.pdmodel.PDDocument.load(pdfFile.getInputStream())) {
//            text = new org.apache.pdfbox.text.PDFTextStripper().getText(doc);
//        }
//
//        int maxChars = 7000 * 2;
//        if (text.length() > maxChars) text = text.substring(0, maxChars);
//
//        return String.format("""
//                당신은 교육 전문가입니다. 다음의 학습 자료를 기반으로 문제를 생성해주세요.
//
//                🏷️ 카테고리: %s
//                📚 챕터: %s
//                📌 문제 유형: %s
//                🔢 생성할 문제 수: %s
//                🧠 추가 지시사항: %s
//
//                아래는 학습 자료입니다. 이 자료를 분석하여 요청한 유형과 수량에 맞게 문제를 생성해주세요.
//                가능하면 다양한 유형(객관식, 단답형, 서술형 등)을 포함하고, 문제는 각기 다른 내용을 다루도록 해주세요.
//
//                [학습자료 시작]
//                %s
//                [학습자료 끝]
//                """, category, chapter, type, count, userPrompt, text);
//    }

    @GetMapping("/generate-test")
    public ResponseEntity<List<QuizDTO>> generateTest() {
        try {
            // 🔹 하드코딩된 테스트용 프롬프트 구성
            String category = "자바";
            String chapter = "변수";
            String type = "객관식";
            String count = "3";
            String userPrompt = "보기 4개 포함, 각 문제에 정답과 해설도 넣어주세요.";

            String sampleText = """
                    변수는 데이터를 저장하는 메모리 공간입니다.
                    자바에서 변수를 선언할 때는 타입과 변수명을 함께 지정해야 합니다.
                    예: int score = 100;
                    변수는 값을 재사용하거나 수정할 수 있도록 도와줍니다.
                    """;

            String finalPrompt = String.format("""
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
                """, category, chapter, type, count, userPrompt, sampleText);

            // 🔹 하드코딩된 prompt만 넘기고 PDF는 null
            List<QuizDTO> result = quizService.generateQuizFromPdf(null, finalPrompt);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace(); // 진짜 원인 콘솔에 출력
            return ResponseEntity.internalServerError().body(null);
        }
    }
}

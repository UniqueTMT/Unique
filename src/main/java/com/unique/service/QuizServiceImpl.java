package com.unique.service;
import com.unique.dto.QuizDTO;
import com.unique.entity.QuizEntity;
import com.unique.gpt.GPTClient;
import com.unique.gpt.QuizParser;
import com.unique.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final GPTClient gptClient;
    private final QuizRepository quizRepository;
    private final QuizParser quizParser;
    private final ModelMapper modelMapper;


    public List<QuizEntity> svcQuizList() {
        return quizRepository.findAll();
    }

    public Optional<QuizEntity> svcQuizDetail(Long id) {
        return quizRepository.findById(id);
    }

    public void svcQuizInsert(QuizEntity entity) {
        quizRepository.save(entity);
    }

    public void svcQuizUpdate(QuizEntity entity) {
        QuizEntity existing = quizRepository.findById(entity.getQuizSeq())
                .orElseThrow(() -> new RuntimeException("해당 문제를 찾을 수 없습니다."));

        // ✅ 필요한 필드만 업데이트
        existing.setQuiz(entity.getQuiz());
        existing.setObjYn(entity.getObjYn());
        existing.setObj1(entity.getObj1());
        existing.setObj2(entity.getObj2());
        existing.setObj3(entity.getObj3());
        existing.setObj4(entity.getObj4());
        existing.setCorrectAnswer(entity.getCorrectAnswer());
        existing.setCorrectScore(entity.getCorrectScore());
        existing.setHint(entity.getHint());
        existing.setComments(entity.getComments());
        quizRepository.save(existing);
    }

    public void svcQuizDelete(Long id) {
        quizRepository.deleteById(id);
    }

//    public List<QuizDTO> generateQuizFromPdf(MultipartFile file, String prompt) throws IOException {
//
//        // 1. PDF 텍스트 추출
//        String pdfText;
//        try (PDDocument doc = PDDocument.load(file.getInputStream())) {
//            pdfText = new PDFTextStripper().getText(doc);
//        }
//
//        // 2. GPT 호출
//        String fullPrompt = prompt + "\n\n" + pdfText;
//        String gptResponse = gptClient.sendPrompt(fullPrompt);
//
//        // 3. 파싱 → QuizDTO 리스트
//        List<QuizDTO> quizzes = quizParser.parse(gptResponse);
//
//        // 4. 저장 → QuizEntity
//        List<QuizEntity> saved = quizRepository.saveAll(
//                quizzes.stream().map(q -> modelMapper.map(q, QuizEntity.class)).toList()
//        );
//
//        return saved.stream().map(q -> modelMapper.map(q, QuizDTO.class)).toList();
//    }
@Override
public List<QuizDTO> generateQuizFromPdf(MultipartFile file, String prompt) throws IOException {
    String pdfText = "";

    if (file != null) {
        try (PDDocument doc = PDDocument.load(file.getInputStream())) {
            pdfText = new PDFTextStripper().getText(doc);
        }
    }

    // GPT 호출
    String fullPrompt = file != null ? prompt + "\n\n" + pdfText : prompt;
    String gptResponse = gptClient.sendPrompt(fullPrompt);

    // 파싱 및 저장
    List<QuizDTO> quizzes = quizParser.parse(gptResponse);
    List<QuizEntity> saved = quizRepository.saveAll(
            quizzes.stream().map(q -> modelMapper.map(q, QuizEntity.class)).toList()
    );

    return saved.stream().map(q -> modelMapper.map(q, QuizDTO.class)).toList();
}


}

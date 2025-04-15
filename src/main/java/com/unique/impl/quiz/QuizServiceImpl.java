package com.unique.impl.quiz;
import com.unique.dto.quiz.QuizDTO;
import com.unique.entity.quiz.QuizEntity;
import com.unique.gpt.GPTClient;
import com.unique.repository.quiz.QuizRepository;
import com.unique.service.quiz.QuizService;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

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

}

package com.unique.service;
import com.unique.entity.QuizEntity;
import com.unique.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;

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
        quizRepository.save(entity);
    }

    public void svcQuizDelete(Long id) {
        quizRepository.deleteById(id);
    }
}

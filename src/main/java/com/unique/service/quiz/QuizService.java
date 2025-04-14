package com.unique.service.quiz;

import com.unique.dto.quiz.QuizDTO;
import com.unique.entity.quiz.QuizEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
public interface QuizService {
    List<QuizEntity> svcQuizList();
    Optional<QuizEntity> svcQuizDetail(Long id);
    void svcQuizInsert(QuizEntity entity);
    void svcQuizUpdate(QuizEntity entity);
    void svcQuizDelete(Long id);
    List<QuizDTO> generateQuizFromPdf(MultipartFile file, String prompt) throws IOException;

}

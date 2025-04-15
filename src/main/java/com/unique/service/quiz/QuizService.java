package com.unique.service.quiz;

import com.unique.dto.quiz.QuizDTO;
import com.unique.entity.quiz.QuizEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
public interface QuizService {
    Optional<QuizEntity> svcQuizDetail(Long id);
    void svcQuizInsert(QuizEntity entity);

    List<QuizDTO> svcGetQuizList(Long examSeq);
    void svcUpdateQuiz(Long quizSeq, QuizDTO dto);
    void svcDeleteQuiz(Long quizSeq);
    void svcPublishExam(Long examSeq);
}

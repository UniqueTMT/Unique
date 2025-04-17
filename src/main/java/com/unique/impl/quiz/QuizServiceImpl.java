package com.unique.impl.quiz;
import com.unique.dto.quiz.QuizDTO;
import com.unique.entity.exam.ExamEntity;
import com.unique.entity.quiz.QuizEntity;
import com.unique.repository.exam.ExamRepository;
import com.unique.repository.quiz.QuizRepository;
import com.unique.service.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//
//@Service
//@RequiredArgsConstructor
//public class QuizServiceImpl implements QuizService {
//    private final QuizRepository quizRepository;
//    private final ModelMapper modelMapper;
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public List<QuizEntity> svcQuizList() {
//        return quizRepository.findAll();
//    }
//
//    public Optional<QuizEntity> svcQuizDetail(Long id) {
//        return quizRepository.findById(id);
//    }
//
//    public void svcQuizInsert(QuizEntity entity) {
//        quizRepository.save(entity);
//    }
//
//    public void svcQuizUpdate(QuizEntity entity) {
//        QuizEntity existing = quizRepository.findById(entity.getQuizSeq())
//                .orElseThrow(() -> new RuntimeException("해당 문제를 찾을 수 없습니다."));
//
//        // ✅ 필요한 필드만 업데이트
//        existing.setQuiz(entity.getQuiz());
//        existing.setObjYn(entity.getObjYn());
//        existing.setObj1(entity.getObj1());
//        existing.setObj2(entity.getObj2());
//        existing.setObj3(entity.getObj3());
//        existing.setObj4(entity.getObj4());
//        existing.setCorrectAnswer(entity.getCorrectAnswer());
//        existing.setCorrectScore(entity.getCorrectScore());
//        existing.setHint(entity.getHint());
//        existing.setComments(entity.getComments());
//        quizRepository.save(existing);
//    }
//
//    public void svcQuizDelete(Long id) {
//        quizRepository.deleteById(id);
//    }
@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final ExamRepository examRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public List<QuizDTO> svcGetQuizList(Long examSeq) {
        return quizRepository.findByExam_ExamSeq(examSeq)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<QuizEntity> svcQuizDetail(Long id) {
        return quizRepository.findById(id);
    }

    @Override
    public void svcQuizInsert(QuizEntity entity) {
        quizRepository.save(entity);
    }

    private QuizDTO convertToDto(QuizEntity entity) {
        return QuizDTO.builder()
                .quizSeq(entity.getQuizSeq())
                .examSeq(entity.getExam().getExamSeq())
                .quiz(entity.getQuiz())
//                .objYn(entity.getObjYn())
                .obj1(entity.getObj1())
                .obj2(entity.getObj2())
                .obj3(entity.getObj3())
                .obj4(entity.getObj4())
                .correctAnswer(entity.getCorrectAnswer())
                .correctScore(entity.getCorrectScore())
                .hint(entity.getHint())
                .comments(entity.getComments())
                .build();
    }


    @Override
    public void svcUpdateQuiz(Long quizSeq, QuizDTO dto) {
        QuizEntity quiz = quizRepository.findById(quizSeq).orElseThrow();
        quiz.setQuiz(dto.getQuiz());
        quiz.setCorrectAnswer(dto.getCorrectAnswer());
        quiz.setObj1(dto.getObj1());
        quiz.setObj2(dto.getObj2());
        quiz.setObj3(dto.getObj3());
        quiz.setObj4(dto.getObj4());
        quiz.setObjYn(dto.getObjYn());
        quiz.setCorrectScore(dto.getCorrectScore());
        quiz.setHint(dto.getHint());
        quiz.setComments(dto.getComments());
        quizRepository.save(quiz);
    }

    @Override
    public void svcDeleteQuiz(Long quizSeq) {
        quizRepository.deleteById(quizSeq);
    }

    @Override
    public void svcPublishExam(Long examSeq) {
        ExamEntity exam = examRepository.findById(examSeq).orElseThrow();
        exam.setPubYn("1");
        examRepository.save(exam);
        kafkaTemplate.send("exam-published", examSeq.toString());
    }
}

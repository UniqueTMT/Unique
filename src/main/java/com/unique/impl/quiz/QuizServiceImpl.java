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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final ExamRepository examRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

//    @Override
//    public List<QuizDTO> svcGetQuizList(Long examSeq) {
//        return quizRepository.findByExam_ExamSeq(examSeq)
//                .stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Optional<QuizEntity> svcQuizDetail(Long id) {
//        return quizRepository.findById(id);
//    }
//
//    @Override
//    public void svcQuizInsert(QuizEntity entity) {
//        quizRepository.save(entity);
//    }
//
//    private QuizDTO convertToDto(QuizEntity entity) {
//        return QuizDTO.builder()
//                .quizSeq(entity.getQuizSeq())
//                .examSeq(entity.getExam().getExamSeq())
//                .quiz(entity.getQuiz())
////                .objYn(entity.getObjYn())
//                .obj1(entity.getObj1())
//                .obj2(entity.getObj2())
//                .obj3(entity.getObj3())
//                .obj4(entity.getObj4())
//                .correctAnswer(entity.getCorrectAnswer())
//                .correctScore(entity.getCorrectScore())
//                .hint(entity.getHint())
//                .comments(entity.getComments())
//                .build();
//    }
//
//
//    @Override
//    public void svcUpdateQuiz(Long quizSeq, QuizDTO dto) {
//        QuizEntity quiz = quizRepository.findById(quizSeq).orElseThrow();
//        quiz.setQuiz(dto.getQuiz());
//        quiz.setCorrectAnswer(dto.getCorrectAnswer());
//        quiz.setObj1(dto.getObj1());
//        quiz.setObj2(dto.getObj2());
//        quiz.setObj3(dto.getObj3());
//        quiz.setObj4(dto.getObj4());
//        quiz.setObjYn(dto.getObjYn());
//        quiz.setCorrectScore(dto.getCorrectScore());
//        quiz.setHint(dto.getHint());
//        quiz.setComments(dto.getComments());
//        quizRepository.save(quiz);
//    }
//
//    @Override
//    public void svcDeleteQuiz(Long quizSeq) {
//        quizRepository.deleteById(quizSeq);
//    }
//
//    @Override
//    public void svcPublishExam(Long examSeq) {
//        ExamEntity exam = examRepository.findById(examSeq).orElseThrow();
//        exam.setPubYn("1");
//        examRepository.save(exam);
//        kafkaTemplate.send("exam-published", examSeq.toString());
//    }
// 문제 목록 조회
@Override
public List<Map<String, Object>> svcGetQuizListMap(Long examSeq) {
    List<QuizEntity> quizList = quizRepository.findByExam_ExamSeq(examSeq);
    List<Map<String, Object>> result = new ArrayList<>();

    for (QuizEntity quiz : quizList) {
        Map<String, Object> map = new HashMap<>();
        map.put("quizSeq", quiz.getQuizSeq());
        map.put("quiz", quiz.getQuiz());
        map.put("obj1", quiz.getObj1());
        map.put("obj2", quiz.getObj2());
        map.put("obj3", quiz.getObj3());
        map.put("obj4", quiz.getObj4());
        map.put("correctAnswer", quiz.getCorrectAnswer());
        map.put("correctScore", quiz.getCorrectScore());
        map.put("hint", quiz.getHint());
        map.put("comments", quiz.getComments());
        map.put("objYn", quiz.getObjYn());
        map.put("regdate", quiz.getRegdate());
        result.add(map);
    }

    return result;
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


    // 문제 수정
    @Override
    public void svcUpdateQuizMap(Long quizSeq, Map<String, Object> params) {
        QuizEntity quiz = quizRepository.findById(quizSeq)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        quiz.setQuiz((String) params.get("quiz"));
        quiz.setObj1((String) params.get("obj1"));
        quiz.setObj2((String) params.get("obj2"));
        quiz.setObj3((String) params.get("obj3"));
        quiz.setObj4((String) params.get("obj4"));
        quiz.setCorrectAnswer((String) params.get("correctAnswer"));

        // ✅ 수정된 부분
        Object correctScoreObj = params.get("correctScore");
        if (correctScoreObj != null) {
            quiz.setCorrectScore(Integer.parseInt(correctScoreObj.toString()));
        } else {
            quiz.setCorrectScore(0);
        }

        quiz.setHint((String) params.get("hint"));
        quiz.setComments((String) params.get("comments"));

        quizRepository.save(quiz);
    }

    //문제 저장
    @Override
    public void svcSaveAllQuiz(List<QuizDTO> quizList) {
        for (QuizDTO dto : quizList) {
            QuizEntity quiz = quizRepository.findById(dto.getQuizSeq())
                    .orElseThrow(() -> new IllegalArgumentException("Quiz not found: " + dto.getQuizSeq()));

            // DTO → Entity 데이터 매핑
            quiz.setQuiz(dto.getQuiz());
            quiz.setObj1(dto.getObj1());
            quiz.setObj2(dto.getObj2());
            quiz.setObj3(dto.getObj3());
            quiz.setObj4(dto.getObj4());
            quiz.setCorrectAnswer(dto.getCorrectAnswer());
            quiz.setCorrectScore(dto.getCorrectScore());
            quiz.setHint(dto.getHint());
            quiz.setComments(dto.getComments());

            // 저장
            quizRepository.save(quiz);
        }
    }


    //문제 삭제
    @Override
    public void svcDeleteQuiz(Long quizSeq) {
        quizRepository.deleteById(quizSeq);
    }


    //출제완료
    @Override
    public void svcPublishExam(Long examSeq) {
        ExamEntity exam = examRepository.findById(examSeq)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        exam.setPubYn("Y"); // 출제 완료 상태를 "Y"로 표시
        examRepository.save(exam);
    }
}

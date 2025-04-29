package com.unique.impl.answer;

import com.unique.dto.answer.AnswerDTO;
import com.unique.dto.answer.AnswerDetailDTO;
import com.unique.dto.answer.StudentExamResultDTO;
import com.unique.kafka.*;
import com.unique.entity.answer.AnswerEntity;
import com.unique.entity.applys.ApplysEntity;
import com.unique.entity.quiz.QuizEntity;
import com.unique.kafka.KafkaAnswerProducer;
import com.unique.repository.answer.AnswerRepository;
import com.unique.repository.applys.ApplysRepository;
import com.unique.repository.quiz.QuizRepository;
import com.unique.service.answer.AnswerService;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final ModelMapper modelMapper;
    private final ApplysRepository applysRepository;
    private final QuizRepository quizRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final KafkaAnswerProducer kafkaProducer;

    public List<AnswerEntity> svcGetAllAnswers() {
        return answerRepository.findAll();
    }

    public Optional<AnswerEntity> svcGetAnswer(Long id) {
        return answerRepository.findById(id);
    }

    public void svcCreateAnswer(AnswerEntity answer) {
        answerRepository.save(answer);
    }

    public void svcDeleteAnswer(Long id) {
        answerRepository.deleteById(id);
    }


    //응시자 답안 확인
    public List<AnswerDTO> svcGetAllMembersAnswers(){
        return answerRepository.findGetAllMembersAnswers().stream()
                .map(answer -> modelMapper.map(answer, AnswerDTO.class))
                .collect(Collectors.toList());

    }

    //임의의 학생 시험 결과 확인
    @Override
    public List<StudentExamResultDTO> svcFindStudentExamResultsByUserid(Long userid) {
        return answerRepository.findStudentExamResultsByUserid(userid);
    }

    public List<AnswerDetailDTO> svcFindSelectedStudentResult(Long userid) {
        return answerRepository.findSelectedStudentResult(userid).stream()
                .map(answer -> modelMapper.map(answer, AnswerDetailDTO.class))
                .collect(Collectors.toList());
    }

    // 응시 답안 제출 -> 저장 -> 채점
    public void saveOrUpdateAnswer(AnswerDTO answerDTO) {
        Optional<AnswerEntity> existingAnswerOpt = answerRepository
            .findByApplys_ApplysSeqAndQuiz_QuizSeq(answerDTO.getApplysSeq(), answerDTO.getQuizSeq());

        AnswerEntity answerEntity;

        if (existingAnswerOpt.isPresent()) {
            answerEntity = existingAnswerOpt.get();
            answerEntity.setUserAnswer(answerDTO.getUserAnswer());
            answerEntity.setRegdate(new Date());
        } else {
            ApplysEntity applys = applysRepository.findById(answerDTO.getApplysSeq())
                .orElseThrow(() -> new RuntimeException("응시 내역 없음"));
            QuizEntity quiz = quizRepository.findById(answerDTO.getQuizSeq())
                .orElseThrow(() -> new RuntimeException("문제 없음"));

            answerEntity = AnswerEntity.builder()
                .applys(applys)
                .quiz(quiz)
                .userAnswer(answerDTO.getUserAnswer())
                .answerYn("")
                .confirmed(false)
                .regdate(new Date())
                .build();
        }

        // 1. 먼저 답안 저장
        AnswerEntity saved = answerRepository.save(answerEntity);

        // 2. Redis에서 해당 문제의 objYn 확인 (객관식/주관식 구분)
        String redisKey = "room:" + answerDTO.getRoomSeq() + ":quiz:" + answerDTO.getQuizSeq();
        String objYn = (String) redisTemplate.opsForHash().get(redisKey, "objYn");

        if ("1".equals(objYn)) {
            // 객관식일 경우 → 즉시 채점
            String correctAnswer = (String) redisTemplate.opsForHash().get(redisKey, "correct");
            boolean isCorrect = correctAnswer != null && correctAnswer.equals(answerDTO.getUserAnswer());

            String scoreStr = (String) redisTemplate.opsForHash().get(redisKey, "score");
            int score = 0;
            try {
                score = isCorrect ? Integer.parseInt(scoreStr) : 0;
            } catch (NumberFormatException e) {
                score = 0;
            }

            saved.setAnswerYn(isCorrect ? "Y" : "N");

            ApplysEntity applys = saved.getApplys();
            if (applys.getTotalScore() == null) applys.setTotalScore(0);
            if (applys.getCorrectCount() == null) applys.setCorrectCount(0);
            if (applys.getWrongCount() == null) applys.setWrongCount(0);

            if (isCorrect) {
                applys.setCorrectCount(applys.getCorrectCount() + 1);
                applys.setTotalScore(applys.getTotalScore() + score);
            } else {
                applys.setWrongCount(applys.getWrongCount() + 1);
            }

            answerRepository.save(saved);
        } else {
            // 주관식 또는 혼합 → Kafka로 GPT 채점 요청
            kafkaProducer.sendToGptGradingTopic(
                AnswerKafkaDTO.builder()        // AnswerKafkaDTO dto
                    .answerSeq(saved.getAnswerSeq())
                    .applysSeq(saved.getApplys().getApplysSeq())
                    .quizSeq(saved.getQuiz().getQuizSeq())
                    .roomSeq(answerDTO.getRoomSeq())
                    .userAnswer(saved.getUserAnswer())
                    .build()
            );
        }
    }

    // 2차 채점 로직
    public void confirmGrading(AnswerConfirmDTO dto) {
        AnswerEntity answer = answerRepository.findById(dto.getAnswerSeq())
            .orElseThrow(() -> new RuntimeException("답안을 찾을 수 없습니다."));

        // 1. 교수 채점 결과 저장
        answer.setProfessorScore(dto.getProfessorScore());
        answer.setProfessorFeedback(dto.getProfessorFeedback());
        answer.setConfirmed(true);

        // 2. answerYn 도출 로직 (교수 점수가 기준 점수 넘는지 판단해서 확정)
        int finalScore = dto.getProfessorScore();
        answer.setAnswerYn(finalScore >= 60 ? "Y" : "N");

        answerRepository.save(answer);

        // 3. ApplysEntity 점수 반영 (확정일 경우만)
        ApplysEntity applys = answer.getApplys();
        if (applys.getTotalScore() == null) applys.setTotalScore(0);
        if (applys.getCorrectCount() == null) applys.setCorrectCount(0);
        if (applys.getWrongCount() == null) applys.setWrongCount(0);

        if (finalScore >= 60) {
            applys.setCorrectCount(applys.getCorrectCount() + 1);
            applys.setTotalScore(applys.getTotalScore() + finalScore);
        } else {
            applys.setWrongCount(applys.getWrongCount() + 1);
        }

        applysRepository.save(applys);
    }




}

package com.unique.impl.answer;

import com.unique.dto.answer.*;
import com.unique.dto.applys.ApplyCheckDTO;
import com.unique.entity.answer.AnswerEntity;
import com.unique.entity.applys.ApplysEntity;
import com.unique.entity.quiz.QuizEntity;
import com.unique.kafka.AnswerConfirmDTO;
import com.unique.repository.answer.AnswerRepository;
import com.unique.repository.applys.ApplysRepository;
import com.unique.repository.quiz.QuizRepository;
import com.unique.service.answer.AnswerService;
import com.unique.dto.gpt.GPTScoreResult;

import com.unique.service.gpt.GptService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final ModelMapper modelMapper;
    private final ApplysRepository applysRepository;
    private final QuizRepository quizRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final GptService gptService; // GPT 채점 서비스 주입

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

    public List<AnswerDTO> svcGetAllMembersAnswers() {
        List<AnswerEntity> answers = answerRepository.findGetAllMembersAnswers();
        List<AnswerDTO> dtos = new ArrayList<>();
        for (AnswerEntity answer : answers) {
            dtos.add(modelMapper.map(answer, AnswerDTO.class));
        }
        return dtos;
    }

    public List<StudentExamResultDTO> svcFindStudentExamResultsByUserid(Long userid) {
        return answerRepository.findStudentExamResultsByUserid(userid);
    }

    public List<AnswerDetailDTO> svcFindSelectedStudentResult(Long userid) {
        List<AnswerDetailDTO> result = new ArrayList<>();
        List<AnswerEntity> answers = answerRepository.findSelectedStudentResult(userid);
        for (AnswerEntity answer : answers) {
            result.add(modelMapper.map(answer, AnswerDetailDTO.class));
        }
        return result;
    }

    // 2차 채점 (출제자 확정 채점)
    @Transactional
    public void confirmGrading(AnswerConfirmDTO dto) {
        List<ApplyCheckDTO> dtoList = dto.getApplyCheckList();
        Map<Long, ApplysEntity> applysMap = new HashMap<>();

        for (ApplyCheckDTO check : dtoList) {
            AnswerEntity answer = answerRepository.findById(check.getAnswerSeq())
                .orElseThrow(() -> new RuntimeException("답안을 찾을 수 없습니다."));

            answer.setProfessorScore(check.getProfessorScore());
            answer.setProfessorFeedback(check.getProfessorFeedback());
            answer.setConfirmed(true);

            int score = check.getProfessorScore();
            answer.setAnswerYn(score >= 60 ? "Y" : "N");

            answerRepository.save(answer);

            ApplysEntity applys = answer.getApplys();
            applysMap.put(applys.getApplysSeq(), applys); // 중복 방지용
        }

        // ApplysEntity 통계 재계산
        for (ApplysEntity applys : applysMap.values()) {
            List<AnswerEntity> answers = answerRepository.findByApplys(applys);

            int total = 0;
            int correct = 0;
            int wrong = 0;

            for (AnswerEntity ans : answers) {
                if (ans.getProfessorScore() != null) {
                    total += ans.getProfessorScore();
                    if ("Y".equals(ans.getAnswerYn())) correct++;
                    else wrong++;
                }
            }

            applys.setTotalScore(total);
            applys.setCorrectCount(correct);
            applys.setWrongCount(wrong);

            applysRepository.save(applys);
        }
    }




    @Override
    public void saveAllAnswers(AnswerSubmitDTO submitDTO, Long applysSeq) {
        for (AnswerSubmitDTO.AnswerDTO dto : submitDTO.getAnswers()) {
            AnswerDTO answerDTO = new AnswerDTO();
            answerDTO.setApplysSeq(applysSeq);
            answerDTO.setQuizSeq(dto.getQuizSeq());
            answerDTO.setUserAnswer(dto.getAnswer());
            answerDTO.setRoomSeq(submitDTO.getRoomSeq());
            answerDTO.setUserSeq(submitDTO.getUserSeq());

            saveOrUpdateAnswer(answerDTO);
        }
    }

    // 수정된 답안 제출 및 채점 로직 (Kafka 제거, Redis 기반, GPT 직접 호출)
    @Override
    public void saveOrUpdateAnswer(AnswerDTO dto) {
        // 1. userSeq + roomSeq 기반으로 applys 조회
        ApplysEntity applys = applysRepository.findByUserSeqAndRoomSeq(dto.getUserSeq(), dto.getRoomSeq())
            .orElseThrow(() -> new IllegalArgumentException("응시 정보가 없습니다."));

        // 2. 해당 응시자가 제출한 해당 문제 답안 조회
        Optional<AnswerEntity> existingOpt = answerRepository.findByApplys_ApplysSeqAndQuiz_QuizSeq(applys.getApplysSeq(), dto.getQuizSeq());

        AnswerEntity answer = existingOpt.map(existing -> {
            existing.setUserAnswer(dto.getUserAnswer());
            existing.setRegdate(new Date());
            return existing;
        }).orElseGet(() -> {
            QuizEntity quiz = quizRepository.findById(dto.getQuizSeq())
                .orElseThrow(() -> new IllegalArgumentException("퀴즈가 존재하지 않습니다."));

            return AnswerEntity.builder()
                .applys(applys)
                .quiz(quiz)
                .userAnswer(dto.getUserAnswer())
                .regdate(new Date())
                .answerYn("")
                .confirmed(false)
                .userSeq(dto.getUserSeq())      // 추가
                .roomSeq(dto.getRoomSeq())      // 추가
                .build();
        });

        // 수정 시에도 필드 반영
        answer.setUserSeq(dto.getUserSeq());   // 추가
        answer.setRoomSeq(dto.getRoomSeq());   // 추가

        // 3. Redis 채점
        String redisKey = "room:" + dto.getRoomSeq() + ":quiz:" + dto.getQuizSeq();
        String objYn = (String) redisTemplate.opsForHash().get(redisKey, "objYn");
        boolean isObjective = "1".equals(objYn) || "Y".equalsIgnoreCase(objYn);

        if (isObjective) {
            String correct = (String) redisTemplate.opsForHash().get(redisKey, "correct");
            String scoreStr = (String) redisTemplate.opsForHash().get(redisKey, "score");
            boolean isCorrect = correct != null && correct.equals(dto.getUserAnswer());
            int score = isCorrect ? Integer.parseInt(scoreStr) : 0;

            answer.setAnswerYn(isCorrect ? "Y" : "N");
            answer.setAiScore(score);
            answer.setAiFeedback(isCorrect ? "정답입니다." : "오답입니다.");
        } else {
            String reference = (String) redisTemplate.opsForHash().get(redisKey, "correct");
            String scoreStr = (String) redisTemplate.opsForHash().get(redisKey, "score");
            int fullScore = scoreStr != null ? Integer.parseInt(scoreStr) : 100;

            GPTScoreResult result = gptService.gradeAnswer(dto.getUserAnswer(), reference, fullScore);

            answer.setAiScore(result.getScore());
            answer.setAiFeedback(result.getFeedback());
            answer.setAnswerYn(result.getScore() >= 60 ? "Y" : "N");
        }

        // 4. 저장
        answerRepository.save(answer);
    }



    @Override
    public List<AnswerGradingDetailDTO> getGradingDetail(Long userSeq, Long roomSeq) {
        return answerRepository.findGradingDetailByUserAndRoom(userSeq, roomSeq);
    }

}

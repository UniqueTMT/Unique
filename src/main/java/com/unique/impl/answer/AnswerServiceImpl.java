package com.unique.impl.answer;

import com.unique.dto.answer.AnswerDTO;
import com.unique.dto.answer.AnswerDetailDTO;
import com.unique.entity.answer.AnswerEntity;
import com.unique.entity.applys.ApplysEntity;
import com.unique.entity.quiz.QuizEntity;
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

    public List<AnswerEntity> svcGetAllAnswers() {
        return answerRepository.findAll();
    }

    public Optional<AnswerEntity> svcGetAnswer(Long id) {
        return answerRepository.findById(id);
    }

    public void svcCreateAnswer(AnswerEntity answer) {
        answerRepository.save(answer);
    }

    public void svcUpdateAnswer(AnswerEntity answer) {
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
    public List<AnswerDetailDTO> svcFindSelectedStudentResult(Long userid) {
        return answerRepository.findSelectedStudentResult(userid).stream()
                .map(answer -> modelMapper.map(answer, AnswerDetailDTO.class))
                .collect(Collectors.toList());
    }

    // 응시 답안 제출 -> 저장 -> 채점
    public void saveOrUpdateAnswer(AnswerDTO answerDTO) {
        // 1. 해당 applySeq + quizSeq 조합으로 AnswerEntity 조회
        Optional<AnswerEntity> existingAnswerOpt = answerRepository
            .findByApplys_ApplysSeqAndQuiz_QuizSeq(answerDTO.getApplysSeq(), answerDTO.getQuizSeq());

        AnswerEntity answerEntity;

        if (existingAnswerOpt.isPresent()) {
            // 2. 존재하면 Update
            answerEntity = existingAnswerOpt.get();
            answerEntity.setUserAnswer(answerDTO.getUserAnswer());
            answerEntity.setRegdate(new Date()); // 수정 시점 업데이트
        } else {
            // 3. 존재하지 않으면 Insert
            ApplysEntity applys = applysRepository.findById(answerDTO.getApplysSeq())
                .orElseThrow(() -> new RuntimeException("응시 내역 없음"));

            QuizEntity quiz = quizRepository.findById(answerDTO.getQuizSeq())
                .orElseThrow(() -> new RuntimeException("문제 없음"));

            answerEntity = AnswerEntity.builder()
                .applys(applys)     // 어떤 학생이 푼 문제인지
                .quiz(quiz)         // 위에서 찾은 문제
                .userAnswer(answerDTO.getUserAnswer())  // 사용자의 실제 선택 답안
                .answerYn("N") // 채점 전
                .regdate(new Date())    // 제출 시각
                .build();
        }

        // Redis 정답 비교
        String redisKey = "room:" + answerDTO.getRoomSeq() + ":quiz:" + answerDTO.getQuizSeq();
        String correctAnswer = (String) redisTemplate.opsForHash().get(redisKey, "correct");

        boolean isCorrect = correctAnswer != null && correctAnswer.equals(answerDTO.getUserAnswer());

        answerEntity.setAnswerYn(isCorrect ? "Y" : "N");

        // 점수 처리
        String scoreStr = (String) redisTemplate.opsForHash().get(redisKey, "score");
        int score = 0;
        try {
            score = isCorrect ? Integer.parseInt(scoreStr) : 0;
        } catch (NumberFormatException e) {
            score = 0;
        }

        answerRepository.save(answerEntity);

        // Applys 갱신
        ApplysEntity applys = answerEntity.getApplys();
        if (applys.getTotalScore() == null) applys.setTotalScore(0);
        if (applys.getCorrectCount() == null) applys.setCorrectCount(0);
        if (applys.getWrongCount() == null) applys.setWrongCount(0);

        if (isCorrect) {
            applys.setCorrectCount(applys.getCorrectCount() + 1);
            applys.setTotalScore(applys.getTotalScore() + score);
        } else {
            applys.setWrongCount(applys.getWrongCount() + 1);
        }

        answerRepository.save(answerEntity);
    }


}

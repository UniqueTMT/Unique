package com.unique.kafka;

import com.unique.entity.answer.AnswerEntity;
import com.unique.repository.answer.AnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaAnswerConsumer {

  private final AnswerRepository answerRepository;

  // 토픽 설정, 그룹 설정
  @KafkaListener(topics = "gpt-grading-topic", groupId = "gpt-consumer")
  public void consume(AnswerKafkaDTO message) {
    log.info("GPT 채점 요청 수신: {}", message);

    // 1. 모의 GPT 채점 로직 호출
    int aiScore = simulateGptScore(message.getUserAnswer());
    String aiFeedback = generateMockFeedback(message.getUserAnswer());

    // 2. 답안 엔티티 조회 및 채점 결과 저장
    AnswerEntity answer = answerRepository.findById(message.getAnswerSeq())
        .orElseThrow(() -> new RuntimeException("답안을 찾을 수 없습니다."));

    answer.setAiScore(aiScore);
    answer.setAiFeedback(aiFeedback);
    answer.setAnswerYn(aiScore >= 60 ? "Y" : "N");

    answerRepository.save(answer);

    log.info("GPT 채점 완료: answerSeq={}, score={}, answerYn={}",
        answer.getAnswerSeq(), aiScore, answer.getAnswerYn());
  }

  // 더미 GPT 점수 로직 (답안 길이 기반)
  private int simulateGptScore(String userAnswer) {
    if (userAnswer == null || userAnswer.trim().isEmpty()) return 0;
    return Math.min(100, userAnswer.length() * 3); // 예: 글자 수 * 3점
  }

  // 더미 GPT 피드백 로직
  private String generateMockFeedback(String userAnswer) {
    if (userAnswer.length() > 30) {
      return "내용이 충분하며 논리적입니다.";
    } else if (userAnswer.length() > 10) {
      return "핵심 내용이 일부 부족합니다.";
    } else {
      return "답변이 너무 짧아 평가가 어렵습니다.";
    }
  }
}

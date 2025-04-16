package com.unique.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

// Kafka로 답안 메시지를 보내는 서비스
@Service
@RequiredArgsConstructor
public class KafkaAnswerProducer {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  // "gpt-grading-topic" 토픽으로 메시지 전송
  public void sendToGptGradingTopic(AnswerKafkaDTO dto) {
    kafkaTemplate.send("gpt-grading-topic", dto);
  }
}
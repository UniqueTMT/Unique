package com.unique.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamParticipationDTO {
  // 유저 (시험방 만든 사람)
  private Long roomSeq;

  // 시험방 정보
  private String roomName;
  private Integer limitTime;
  private String memberNickname;
  // 웹소켓 구독을 위한 URL
  private String websocketUrl;

  private List<QuizDTO> quizList; // 시험문제 리스트
  private List<Long> applicantUserIds; // 응시자 유저아이디 리스트
}


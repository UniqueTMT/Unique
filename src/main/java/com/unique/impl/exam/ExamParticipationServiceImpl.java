package com.unique.impl.exam;

import com.unique.dto.exam.ExamParticipationDTO;
import com.unique.dto.quiz.QuizDTO;
import com.unique.entity.quiz.QuizEntity;
import com.unique.entity.room.RoomEntity;
import com.unique.repository.exam.ExamParticipationRepository;
import com.unique.service.exam.ExamParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamParticipationServiceImpl implements ExamParticipationService {

  private final ExamParticipationRepository examParticipationRepository;

  @Override
  public ExamParticipationDTO getExamParticipationDetail(Long roomSeq) {
    // 1. 방 정보 + 시험지 + 방 생성자 + 문제 리스트
    RoomEntity roomEntity = examParticipationRepository.findRoomDetailWithExamAndMember(roomSeq);

    // 2. 문제 DTO 변환 (QuizDTO 사용)
    List<QuizDTO> quizList = roomEntity.getExam().getQuizList().stream()
        .map(this::convertToQuizDTO)
        .collect(Collectors.toList());

    // 3. 응시자 userId 가져오기
    List<Long> applicantUserIds = examParticipationRepository
        .findApplicantUserIdsByExam_ExamSeq(roomEntity.getExam().getExamSeq());

    // 4. DTO 조립
    return new ExamParticipationDTO(
        roomEntity.getRoomSeq(),
        roomEntity.getRoomName(),
        roomEntity.getLimitTime(),
        roomEntity.getMember().getNickname(),
        "ws://localhost:5000/ws", // 웹소켓 URL
        quizList,
        applicantUserIds
    );
  }

  private QuizDTO convertToQuizDTO(QuizEntity quizEntity) {
    return new QuizDTO(
        quizEntity.getQuizSeq(),
        quizEntity.getExam().getExamSeq(),
        quizEntity.getQuiz(),
        quizEntity.getObjYn(),
        quizEntity.getObj1(),
        quizEntity.getObj2(),
        quizEntity.getObj3(),
        quizEntity.getObj4(),
        quizEntity.getCorrectScore(),
        quizEntity.getCorrectAnswer(),
        quizEntity.getHint(),
        quizEntity.getComments(),
        quizEntity.getRegdate()
    );
  }
}

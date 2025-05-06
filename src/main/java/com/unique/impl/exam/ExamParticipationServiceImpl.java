package com.unique.impl.exam;

import com.unique.dto.exam.ExamParticipationDTO;
import com.unique.dto.quiz.QuizDTO;
import com.unique.entity.applys.ApplysEntity;
import com.unique.entity.exam.ExamEntity;
import com.unique.entity.member.MemberEntity;
import com.unique.entity.quiz.QuizEntity;
import com.unique.entity.room.RoomEntity;
import com.unique.repository.applys.ApplysRepository;
import com.unique.repository.exam.ExamParticipationRepository;
import com.unique.repository.member.MemberRepository;
import com.unique.service.exam.ExamParticipationService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamParticipationServiceImpl implements ExamParticipationService {

  private final ExamParticipationRepository examParticipationRepository;
  private final ApplysRepository applysRepository;
  private final MemberRepository memberRepository;

  @Override
  public ExamParticipationDTO getExamParticipationDetail(Long roomSeq, Long userSeq) {
    RoomEntity roomEntity = examParticipationRepository.findRoomDetailWithExamAndMember(roomSeq);

    // 응시자 등록이 안되어 있다면 Applys 생성
    Optional<ApplysEntity> existing = applysRepository.findByUserSeqAndRoomSeq(userSeq, roomSeq);
    if (existing.isEmpty()) {
      ExamEntity exam = roomEntity.getExam();
      MemberEntity member = memberRepository.findById(userSeq).orElseThrow();

      ApplysEntity apply = ApplysEntity.builder()
          .member(member)
          .exam(exam)
          .room(roomEntity)
          .totalScore(0)
          .correctCount(0)
          .wrongCount(0)
          .build();

      applysRepository.save(apply);
    }

    List<QuizDTO> quizList = roomEntity.getExam().getQuizList().stream()
        .map(this::convertToQuizDTO)
        .collect(Collectors.toList());

    List<Long> applicantUserIds = examParticipationRepository
        .findApplicantUserIdsByExam_ExamSeq(roomEntity.getExam().getExamSeq());

    return new ExamParticipationDTO(
        roomEntity.getRoomSeq(),
        roomEntity.getRoomName(),
        roomEntity.getLimitTime(),
        roomEntity.getMember().getNickname(),
        "ws://localhost:5000/ws",
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

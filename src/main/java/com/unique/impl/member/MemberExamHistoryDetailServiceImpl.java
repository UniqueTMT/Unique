package com.unique.impl.member;

import com.unique.dto.member.MemberExamHistoryDetailDTO;
import com.unique.dto.answer.AnswerDTO;
import com.unique.dto.quiz.QuizDTO;
import com.unique.entity.answer.AnswerEntity;
import com.unique.entity.applys.ApplysEntity;
import com.unique.entity.exam.ExamEntity;
import com.unique.entity.member.MemberEntity;
import com.unique.entity.quiz.QuizEntity;
import com.unique.repository.member.MemberExamHistoryDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 특정 시험 결과 데이터를 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
public class MemberExamHistoryDetailServiceImpl {

    private final MemberExamHistoryDetailRepository memberExamHistoryDetailRepository;


    /**
     * 특정 유저(userSeq)가 응시한 특정 시험지(examSeq)의 결과출력 - 경준
     */
    public MemberExamHistoryDetailDTO svcGetExamResult(Long userSeq, Long examSeq) {
        // 1. 기본 정보 조회 (컬렉션 제외)
        ApplysEntity applysEntity = memberExamHistoryDetailRepository.findApplysWithExamAndMember(userSeq, examSeq);
        if (applysEntity == null) throw new RuntimeException("응시 기록 없음");

        // 2. 문제 목록 조회 (별도 쿼리)
        List<QuizEntity> quizList = memberExamHistoryDetailRepository.findQuizzesByExamSeq(examSeq);

        // 3. 답변 목록 조회 (별도 쿼리)
        List<AnswerEntity> answerList = memberExamHistoryDetailRepository.findAnswersByApplysSeq(applysEntity.getApplysSeq());

        // 4. DTO 변환
        return convertToDTO(applysEntity, quizList, answerList);
    }

    private MemberExamHistoryDetailDTO convertToDTO(
            ApplysEntity applys,
            List<QuizEntity> quizList,
            List<AnswerEntity> answerList
    ) {
        ExamEntity exam = applys.getExam();
        MemberEntity member = applys.getMember();

        MemberExamHistoryDetailDTO dto = new MemberExamHistoryDetailDTO();
        dto.setExamSeq(exam.getExamSeq());
        dto.setSubjectCode(exam.getSubjectCode());
        dto.setSubjectName(exam.getSubjectName());
        dto.setExamTitle(exam.getExamTitle());
        dto.setUserSeq(member.getUserSeq());
        dto.setCreatorName(exam.getMember().getUsername());
        dto.setUserid(member.getUserid());
        dto.setUserName(member.getUsername());
        dto.setApplysSeq(applys.getApplysSeq());
        dto.setRegdate(applys.getRegdate());

        // 퀴즈 및 답변 DTO 변환
        List<QuizDTO> quizDTOList = quizList.stream()
                .map(this::convertQuiz).toList();

        List<AnswerDTO> answerDTOList = answerList.stream()
                .map(this::convertAnswer).toList();

        // 점수 계산
        int totalScore = quizList.stream()
                .mapToInt(QuizEntity::getCorrectScore).sum();

        Map<Long, Integer> scoreMap = quizList.stream()
                .collect(Collectors.toMap(QuizEntity::getQuizSeq, QuizEntity::getCorrectScore));

        int obtainedScore = answerList.stream()
                .filter(ans -> "1".equals(ans.getAnswerYn()))
                .mapToInt(ans -> scoreMap.getOrDefault(ans.getQuiz().getQuizSeq(), 0))
                .sum();

        dto.setTotalScore(totalScore);
        dto.setObtainedScore(obtainedScore);
        dto.setQuizList(quizDTOList);
        dto.setAnswerList(answerDTOList);

        return dto;
    }

    private QuizDTO convertQuiz(QuizEntity entity) {
        return new QuizDTO(
                entity.getQuizSeq(),
                entity.getExam().getExamSeq(),
                entity.getQuiz(),
                entity.getObjYn() != null ? String.valueOf(entity.getObjYn().charAt(0)) : null,
                entity.getObj1(),
                entity.getObj2(),
                entity.getObj3(),
                entity.getObj4(),
                entity.getCorrectScore(),
                entity.getCorrectAnswer(),
                entity.getHint(),
                entity.getComments(),
                entity.getRegdate()
        );
    }

    private AnswerDTO convertAnswer(AnswerEntity entity) {
        // MemberEntity 조회 (ApplysEntity -> MemberEntity)
        MemberEntity member = entity.getApplys().getMember();

        // QuizEntity 조회 (AnswerEntity -> QuizEntity)
        QuizEntity quiz = entity.getQuiz();

        return AnswerDTO.builder()
            .answerSeq(entity.getAnswerSeq())
            .applysSeq(entity.getApplys().getApplysSeq())
            .quizSeq(entity.getQuiz().getQuizSeq())
            .userAnswer(entity.getUserAnswer())
            .answerYn(entity.getAnswerYn() != null ? entity.getAnswerYn().charAt(0) : null)
            .userid(member.getUserid())
            .nickname(member.getNickname())
            .correctScore(quiz.getCorrectScore())
            .regdate(entity.getRegdate())
            .build();
    }


}

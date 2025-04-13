package com.unique.service;

import com.unique.dto.UserExamHistoryDetailDTO;
import com.unique.dto.AnswerDTO;
import com.unique.dto.QuizDTO;
import com.unique.entity.*;
import com.unique.repository.UserExamHistoryDetailRepository;
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
public class UserExamHistoryDetailServiceImpl {

    private final UserExamHistoryDetailRepository userExamHistoryDetailRepository;

    /**
     * 특정 유저(userSeq)가 응시한 특정 시험(examSeq)의 결과를 반환.
     */
//    public UserExamHistoryDetailDTO svcGetExamResult(Long userSeq, Long examSeq) {
//        ApplysEntity applysEntity = userExamHistoryDetailRepository.MyfindUserExamHistoryDetail(userSeq, examSeq);
//
//        if (applysEntity == null) throw new RuntimeException("시험 결과를 찾을 수 없습니다.");
//
//        ExamEntity examEntity = applysEntity.getExam();
//        MemberEntity memberEntity = applysEntity.getMember();
//
//        // DTO 생성 및 기본 정보 설정
//        UserExamHistoryDetailDTO detailDTO = new UserExamHistoryDetailDTO();
//        detailDTO.setExamSeq(examEntity.getExamSeq());
//        detailDTO.setSubjectCode(examEntity.getSubjectCode());
//        detailDTO.setSubjectName(examEntity.getSubjectName());
//        detailDTO.setExamTitle(examEntity.getExamTitle());
//        detailDTO.setUserSeq(memberEntity.getUserSeq());
//        detailDTO.setCreatorName(examEntity.getMember().getUsername());
//        detailDTO.setUserid(memberEntity.getUserid());
//        detailDTO.setUserName(memberEntity.getUsername());
//        detailDTO.setApplysSeq(applysEntity.getApplysSeq());
//        detailDTO.setRegdate(applysEntity.getRegdate());
//
//        // 문제와 답변 매핑
//        List<QuizDTO> quizList = examEntity.getQuizList().stream()
//                .map(this::convertToQuizDTO)
//                .toList();
//
//        List<AnswerDTO> answerList = applysEntity.getAnswerList().stream()
//                .map(this::convertToAnswerDTO)
//                .toList();
//
//        // 총점 계산
//        int totalScore = quizList.stream()
//                .mapToInt(QuizDTO::getCorrectScore)
//                .sum();
//
//        // QuizSeq -> CorrectScore 매핑 생성
//        Map<Long, Integer> quizScoreMap = quizList.stream()
//                .collect(Collectors.toMap(QuizDTO::getQuizSeq, QuizDTO::getCorrectScore));
//
//        // 수정된 획득 점수 계산: Map을 사용하여 quizSeq로 correctScore 찾기
//        int obtainedScore = answerList.stream()
//                .filter(ans -> "1".equals(String.valueOf(ans.getAnswerYn())))
//                .mapToInt(ans -> quizScoreMap.getOrDefault(ans.getQuizSeq(), 0))
//                .sum();
//
//        detailDTO.setTotalScore(totalScore);
//        detailDTO.setObtainedScore(obtainedScore);
//        detailDTO.setQuizList(quizList);
//        detailDTO.setAnswerList(answerList);
//
//        return detailDTO;
//    }
//
//    private QuizDTO convertToQuizDTO(QuizEntity entity) {
//        return new QuizDTO(
//                entity.getQuizSeq(),
//                entity.getExam().getExamSeq(),
//                entity.getQuiz(),
//                entity.getObjYn() != null ? entity.getObjYn().charAt(0) : null,
//                entity.getObj1(),
//                entity.getObj2(),
//                entity.getObj3(),
//                entity.getObj4(),
//                entity.getCorrectScore(),
//                entity.getCorrectAnswer(),
//                entity.getHint(),
//                entity.getComments(),
//                entity.getRegdate()
//        );
//    }
//
//    private AnswerDTO convertToAnswerDTO(AnswerEntity entity) {
//        return new AnswerDTO(
//                entity.getAnswerSeq(),
//                entity.getApplys().getApplysSeq(),
//                entity.getQuiz().getQuizSeq(),
//                entity.getUserAnswer(),
//                entity.getAnswerYn() != null ? entity.getAnswerYn().charAt(0) : null,
//                entity.getRegdate()
//        );
//    }

        /**
         * 특정 유저(userSeq)가 응시한 특정 시험지(examSeq)의 결과출력 - 경준
         */
        public UserExamHistoryDetailDTO svcGetExamResult(Long userSeq, Long examSeq) {
            // 1. 기본 정보 조회 (컬렉션 제외)
            ApplysEntity applysEntity = userExamHistoryDetailRepository.findApplysWithExamAndMember(userSeq, examSeq);
            if (applysEntity == null) throw new RuntimeException("응시 기록 없음");

            // 2. 문제 목록 조회 (별도 쿼리)
            List<QuizEntity> quizList = userExamHistoryDetailRepository.findQuizzesByExamSeq(examSeq);

            // 3. 답변 목록 조회 (별도 쿼리)
            List<AnswerEntity> answerList = userExamHistoryDetailRepository.findAnswersByApplysSeq(applysEntity.getApplysSeq());

            // 4. DTO 변환
            return convertToDTO(applysEntity, quizList, answerList);
        }

        private UserExamHistoryDetailDTO convertToDTO(
                ApplysEntity applys,
                List<QuizEntity> quizList,
                List<AnswerEntity> answerList
        ) {
            ExamEntity exam = applys.getExam();
            MemberEntity member = applys.getMember();

            UserExamHistoryDetailDTO dto = new UserExamHistoryDetailDTO();
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
                    entity.getObjYn() != null ? entity.getObjYn().charAt(0) : null,
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
            return new AnswerDTO(
                    entity.getAnswerSeq(),
                    entity.getApplys().getApplysSeq(),
                    entity.getQuiz().getQuizSeq(),
                    entity.getUserAnswer(),
                    entity.getAnswerYn() != null ? entity.getAnswerYn().charAt(0) : null,
                    entity.getRegdate()
            );
        }


}

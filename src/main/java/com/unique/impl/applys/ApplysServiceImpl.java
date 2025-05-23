package com.unique.impl.applys;

import com.unique.dto.applys.ApplysConfirmStatusDTO;
import com.unique.dto.member.MemberExamHistoryDTO;
import com.unique.entity.applys.ApplysEntity;
import com.unique.entity.quiz.QuizEntity;
import com.unique.repository.applys.ApplysRepository;
import com.unique.repository.exam.ExamSearchRepository;
import com.unique.service.applys.ApplysService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplysServiceImpl implements ApplysService {
    private final ApplysRepository applysRepository;
    private final ModelMapper modelMapper;
    private final ExamSearchRepository examSearchRepository;

    //시험이력리스트 - 경준
    @Override
    public List<MemberExamHistoryDTO> myFindAllExamHistory(Long userSeq) {
        return applysRepository.myFindAllExamHistory(userSeq).stream()
                .map(apply -> modelMapper.map(apply, MemberExamHistoryDTO.class))
                .collect(Collectors.toList());
    }

    //시험 이력 - 정렬
    @Override
    public List<MemberExamHistoryDTO> svcExamHistorySorted(Long userSeq, String sort) {
        List<ApplysEntity> list;
        if ("asc".equalsIgnoreCase(sort)) {
            list = applysRepository.findByMemberUserSeqOrderByRegdateAsc(userSeq);
        } else {
            list = applysRepository.findByMemberUserSeqOrderByRegdateDesc(userSeq);
        }

        return list.stream()
                .map(applys -> modelMapper.map(applys, MemberExamHistoryDTO.class))
                .toList();
    }

    //시험이력 검색 - 경준
    /**
     * 사용자의 시험 이력 검색
     */
    public List<MemberExamHistoryDTO> svcSearchUserExamHistory(
            Long userSeq,
            String subjectName,
            String creatorName,
            String examTitle
    ) {
        List<ApplysEntity> applysList = examSearchRepository.mySearchUserExamHistory(
                userSeq,
                subjectName != null && !subjectName.isEmpty() ? subjectName : null,
                creatorName != null && !creatorName.isEmpty() ? creatorName : null,
                examTitle != null && !examTitle.isEmpty() ? examTitle : null
        );

        return applysList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MemberExamHistoryDTO convertToDTO(ApplysEntity applys) {
        
        // 총 문제 합점
        Integer totalScore = applys.getExam().getQuizList().stream()
                .mapToInt(QuizEntity::getCorrectScore)
                .sum();

        // 획득 점수 계산 (AnswerEntity의 정답 여부 확인)
        int obtainedScore = applys.getAnswerList().stream()
                .filter(ans -> "1".equals(ans.getAnswerYn()))
                .mapToInt(ans -> ans.getQuiz().getCorrectScore())
                .sum();

        return MemberExamHistoryDTO.builder()
                .examSeq(applys.getExam().getExamSeq())
                .subjectCode(applys.getExam().getSubjectCode())
                .subjectName(applys.getExam().getSubjectName())
                .examTitle(applys.getExam().getExamTitle())
                .userSeq(applys.getMember().getUserSeq())
                .creatorName(applys.getExam().getMember().getUsername())
                .userid(applys.getMember().getUserid())
                .userName(applys.getMember().getUsername())
                .applysSeq(applys.getApplysSeq())
                .totalScore(applys.getTotalScore())
                .correctCount(applys.getCorrectCount())
                .wrongCount(applys.getWrongCount())
                .regdate(applys.getRegdate())
                .build();
    }

    // 시험 방 관리 - 응시한 유저 수 조회
    @Override
    public List<ApplysConfirmStatusDTO> getConfirmStatusByRoom(Long roomSeq) {
        return applysRepository.findApplysStatusByRoom(roomSeq);
    }


    public List<ApplysEntity> svcApplysList() {
        return applysRepository.findAll();
    }

    public Optional<ApplysEntity> svcApplysDetail(Long id) {
        return applysRepository.findById(id);
    }

    public void svcApplysInsert(ApplysEntity entity) {
        applysRepository.save(entity);
    }

    public void svcApplysUpdate(ApplysEntity entity) {
        applysRepository.save(entity);
    }

    public void svcApplysDelete(Long id) {
        applysRepository.deleteById(id);
    }

}
package com.unique.impl.exam;
import com.unique.dto.exam.CategoryQuizCountDTO;
import com.unique.dto.exam.ExamDTO;
import com.unique.dto.answer.AnswerDetailDTO;
import com.unique.dto.exam.ExamDetailDTO;
import com.unique.entity.exam.ExamEntity;
import com.unique.repository.exam.ExamRepository;
import com.unique.service.exam.ExamService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final ModelMapper modelMapper;

    // 유저가 생성한 시험지 조회
    @Override
    public List<ExamEntity> getSubjectListByLoginUser(Long userSeq){
        return examRepository.findByUserSeq(userSeq);
    }

    //문제은행 리스트 업
    @Override
    public List<CategoryQuizCountDTO> svcGetQuizCountByCategory() {
        return examRepository.findQuizCountGroupedByCategory();
    }

    //문제은행 카테고리별 시험지 상세 보기
    @Override
    public List<ExamDetailDTO> svcFindExamWithQuizListBySubjectCode(String subjectCode) {
        return examRepository.findExamWithQuizListBySubjectCode(subjectCode);
    }


    @Override
    public List<AnswerDetailDTO> svcFindAll() {
        return examRepository.findAll().stream()
                .map(exam -> modelMapper.map(exam, AnswerDetailDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AnswerDetailDTO> svcFindById(Long id) {
        return examRepository.findById(id)
                .map(exam -> modelMapper.map(exam, AnswerDetailDTO.class));
    }

    @Override
    public void svcInsert(AnswerDetailDTO dto) {
        ExamEntity entity = modelMapper.map(dto, ExamEntity.class);
        examRepository.save(entity);
    }

    @Override
    public void svcUpdate(AnswerDetailDTO dto) {
        ExamEntity entity = modelMapper.map(dto, ExamEntity.class);
        examRepository.save(entity);
    }

    @Override
    public void svcDelete(Long id) {
        examRepository.deleteById(id);
    }

}

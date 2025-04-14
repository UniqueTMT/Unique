package com.unique.impl.exam;


import com.unique.dto.exam.ExamDTO;
import com.unique.dto.answer.AnswerDetailDTO;
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

    //문제은행 카테고리별 시험지 상세 보기
    @Override
    public List<ExamDTO> svcFindExamWithQuizList() {
        return examRepository.findExamWithQuizList().stream()
                .map(exam -> modelMapper.map(exam, ExamDTO.class))
                .collect(Collectors.toList());
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

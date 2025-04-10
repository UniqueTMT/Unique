package com.unique.service;


import com.unique.dto.ExamDTO;
import com.unique.dto.ExamDetailDTO;
import com.unique.dto.TestDTO;
import com.unique.entity.ExamEntity;
import com.unique.repository.ExamRepository;
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

    //사용자 정의
    public List<ExamDTO> myFindAllExamWithQuizzes() {
        return examRepository.myFindAllExamWithQuizzes().stream()
                .map(exam -> modelMapper.map(exam, ExamDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<TestDTO> svcFindAll() {
        return examRepository.findAll().stream()
                .map(exam -> modelMapper.map(exam, TestDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TestDTO> svcFindById(Long id) {
        return examRepository.findById(id)
                .map(exam -> modelMapper.map(exam, TestDTO.class));
    }

    @Override
    public void svcInsert(TestDTO dto) {
        ExamEntity entity = modelMapper.map(dto, ExamEntity.class);
        examRepository.save(entity);
    }

    @Override
    public void svcUpdate(TestDTO dto) {
        ExamEntity entity = modelMapper.map(dto, ExamEntity.class);
        examRepository.save(entity);
    }

    @Override
    public void svcDelete(Long id) {
        examRepository.deleteById(id);
    }

}

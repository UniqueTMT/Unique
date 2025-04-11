package com.unique.service;

import com.unique.dto.TestDTO;
import com.unique.entity.AnswerEntity;
import com.unique.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final ModelMapper modelMapper;

    public List<AnswerEntity> svcGetAllAnswers() {
        return answerRepository.findAll();
    }

    public Optional<AnswerEntity> svcGetAnswer(Long id) {
        return answerRepository.findById(id);
    }

    public void svcCreateAnswer(AnswerEntity answer) {
        answerRepository.save(answer);
    }

    public void svcUpdateAnswer(AnswerEntity answer) {
        answerRepository.save(answer);
    }

    public void svcDeleteAnswer(Long id) {
        answerRepository.deleteById(id);
    }


//    public List<TestDTO> svcTest() {
//        List<AnswerEntity> answerList = answerRepository.myFindExamResultsWithGraph();
//        return answerList.stream()
//                .map(answer -> modelMapper.map(answer, TestDTO.class))
//                .collect(Collectors.toList());
//    }



}

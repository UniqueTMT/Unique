package com.unique.service;

import com.unique.dto.AnswerDTO;
import com.unique.dto.AnswerDetailDTO;
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


    //응시자 답안 확인
    public List<AnswerDTO> svcGetAllMembersAnswers(){
        return answerRepository.findGetAllMembersAnswers().stream()
                .map(answer -> modelMapper.map(answer, AnswerDTO.class))
                .collect(Collectors.toList());

    }

    //임의의 학생 시험 결과 확인
    public List<AnswerDetailDTO> svcFindSelectedStudentResult(Long userid) {
        return answerRepository.findSelectedStudentResult(userid).stream()
                .map(answer -> modelMapper.map(answer, AnswerDetailDTO.class))
                .collect(Collectors.toList());
    }

}

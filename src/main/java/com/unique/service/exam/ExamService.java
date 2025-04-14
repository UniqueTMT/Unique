package com.unique.service.exam;

import com.unique.dto.answer.AnswerDetailDTO;
import com.unique.dto.exam.ExamDTO;

import java.util.List;
import java.util.Optional;

public interface ExamService {

    List<AnswerDetailDTO> svcFindAll();
    Optional<AnswerDetailDTO> svcFindById(Long id);
    void svcInsert(AnswerDetailDTO dto);
    void svcUpdate(AnswerDetailDTO dto);
    void svcDelete(Long id);

    //문제은행 리스트업

    //문제은행 카테고리별 시험지 상세 보기
    List<ExamDTO> svcFindExamWithQuizList();

}

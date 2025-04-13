package com.unique.service;

import com.unique.dto.AnswerDetailDTO;
import com.unique.dto.ExamDTO;

import java.util.List;
import java.util.Optional;

public interface ExamService {

    List<AnswerDetailDTO> svcFindAll();
    Optional<AnswerDetailDTO> svcFindById(Long id);
    void svcInsert(AnswerDetailDTO dto);
    void svcUpdate(AnswerDetailDTO dto);
    void svcDelete(Long id);

    //문제은행 카테고리별 시험지 상세 보기
    List<ExamDTO> svcFindExamWithQuizList();

}

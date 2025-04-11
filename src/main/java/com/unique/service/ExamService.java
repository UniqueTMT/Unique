package com.unique.service;

import com.unique.dto.ExamDTO;
import com.unique.dto.ExamDetailDTO;
import com.unique.dto.TestDTO;
import com.unique.entity.ExamEntity;

import java.util.List;
import java.util.Optional;

public interface ExamService {

    List<TestDTO> svcFindAll();
    Optional<TestDTO> svcFindById(Long id);
    void svcInsert(TestDTO dto);
    void svcUpdate(TestDTO dto);
    void svcDelete(Long id);

    //문제은행 카테고리별 문제 상세 보기
    public List<ExamDTO> myFindAllExamWithQuizzes();

}

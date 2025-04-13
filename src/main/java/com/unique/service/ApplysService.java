package com.unique.service;

import com.unique.dto.UserExamHistoryDTO;
import com.unique.entity.AnswerEntity;
import com.unique.entity.ApplysEntity;

import java.util.List;
import java.util.Optional;


public interface ApplysService {
    List<UserExamHistoryDTO>myFindAllExamHistory(Long userSeq);
    List<ApplysEntity> svcApplysList();
    Optional<ApplysEntity> svcApplysDetail(Long id);
    void svcApplysInsert(ApplysEntity entity);
    void svcApplysUpdate(ApplysEntity entity);
    void svcApplysDelete(Long id);
    
    //시험이력 검색 기능 - 경준
    List<UserExamHistoryDTO> svcSearchUserExamHistory(Long userSeq, String subjectName, String creatorName, String examTitle);
}
package com.unique.service.applys;

import com.unique.dto.member.MemberExamHistoryDTO;
import com.unique.entity.applys.ApplysEntity;

import java.util.List;
import java.util.Optional;


public interface ApplysService {
    List<MemberExamHistoryDTO>myFindAllExamHistory(Long userSeq);
    List<ApplysEntity> svcApplysList();
    Optional<ApplysEntity> svcApplysDetail(Long id);
    void svcApplysInsert(ApplysEntity entity);
    void svcApplysUpdate(ApplysEntity entity);
    void svcApplysDelete(Long id);
    
    //시험이력 검색 기능 - 경준
    List<MemberExamHistoryDTO> svcSearchUserExamHistory(Long userSeq, String subjectName, String creatorName, String examTitle);
}
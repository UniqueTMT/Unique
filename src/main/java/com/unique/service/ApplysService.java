package com.unique.service;

import com.unique.entity.AnswerEntity;
import com.unique.entity.ApplysEntity;

import java.util.List;
import java.util.Optional;


public interface ApplysService {
    List<ApplysEntity> svcApplysList();
    Optional<ApplysEntity> svcApplysDetail(Long id);
    void svcApplysInsert(ApplysEntity entity);
    void svcApplysUpdate(ApplysEntity entity);
    void svcApplysDelete(Long id);
}
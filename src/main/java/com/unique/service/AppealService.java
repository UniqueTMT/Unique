package com.unique.service;

import com.unique.entity.AppealEntity;

import java.util.List;
import java.util.Optional;

public interface AppealService {
    List<AppealEntity> svcAppealList();
    Optional<AppealEntity> svcAppealDetail(Long id);
    void svcAppealInsert(AppealEntity entity);
    void svcAppealUpdate(AppealEntity entity);
    void svcAppealDelete(Long id);
}
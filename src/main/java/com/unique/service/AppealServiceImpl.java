package com.unique.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.unique.entity.AppealEntity;
import com.unique.repository.AppealRepository;

@Service
@RequiredArgsConstructor
public class AppealServiceImpl implements AppealService {
    private final AppealRepository appealRepository;

    public List<AppealEntity> svcAppealList() {
        return appealRepository.findAll();
    }

    public Optional<AppealEntity> svcAppealDetail(Long id) {
        return appealRepository.findById(id);
    }

    public void svcAppealInsert(AppealEntity entity) {
        appealRepository.save(entity);
    }

    public void svcAppealUpdate(AppealEntity entity) {
        appealRepository.save(entity);
    }

    public void svcAppealDelete(Long id) {
        appealRepository.deleteById(id);
    }
}
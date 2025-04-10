package com.unique.service;

import com.unique.entity.ApplysEntity;
import com.unique.repository.ApplysRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.unique.entity.AppealEntity;
import com.unique.repository.AppealRepository;

@Service
@RequiredArgsConstructor
public class ApplysServiceImpl implements ApplysService {
    private final ApplysRepository applysRepository;

    public List<ApplysEntity> svcApplysList() {
        return applysRepository.findAll();
    }

    public Optional<ApplysEntity> svcApplysDetail(Long id) {
        return applysRepository.findById(id);
    }

    public void svcApplysInsert(ApplysEntity entity) {
        applysRepository.save(entity);
    }

    public void svcApplysUpdate(ApplysEntity entity) {
        applysRepository.save(entity);
    }

    public void svcApplysDelete(Long id) {
        applysRepository.deleteById(id);
    }
}
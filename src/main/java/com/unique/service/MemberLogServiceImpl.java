package com.unique.service;
import com.unique.entity.MemberLogEntity;

import com.unique.repository.MemberLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberLogServiceImpl implements MemberLogService {
    private final MemberLogRepository memberLogRepository;

    public List<MemberLogEntity> svcMemberLogList() {
        return memberLogRepository.findAll();
    }

    public Optional<MemberLogEntity> svcMemberLogDetail(Long id) {
        return memberLogRepository.findById(id);
    }

    public void svcMemberLogInsert(MemberLogEntity entity) {
        memberLogRepository.save(entity);
    }

    public void svcMemberLogUpdate(MemberLogEntity entity) {
        memberLogRepository.save(entity);
    }

    public void svcMemberLogDelete(Long id) {
        memberLogRepository.deleteById(id);
    }
}

package com.unique.service;
import com.unique.entity.AnswerEntity;
import com.unique.entity.MemberLogEntity;

import java.util.List;
import java.util.Optional;

public interface MemberLogService {
    List<MemberLogEntity> svcMemberLogList();
    Optional<MemberLogEntity> svcMemberLogDetail(Long id);
    void svcMemberLogInsert(MemberLogEntity entity);
    void svcMemberLogUpdate(MemberLogEntity entity);
    void svcMemberLogDelete(Long id);
}

package com.unique.service;
import com.unique.entity.AnswerEntity;
import com.unique.entity.MemberEntity;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    List<MemberEntity> svcMemberList();
    Optional<MemberEntity> svcMemberDetail(Long id);
    void svcMemberInsert(MemberEntity entity);
    void svcMemberUpdate(MemberEntity entity);
    void svcMemberDelete(Long id);
}

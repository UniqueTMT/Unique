package com.unique.service;

import com.unique.dto.MemberInfoDTO;
import com.unique.entity.MemberEntity;
import com.unique.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    public List<MemberEntity> svcMemberList() {
        return memberRepository.findAll();
    }

    public Optional<MemberEntity> svcMemberDetail(Long id) {
        return memberRepository.findById(id);
    }

    public void svcMemberInsert(MemberEntity entity) {
        memberRepository.save(entity);
    }

    public void svcMemberUpdate(MemberEntity entity) {
        memberRepository.save(entity);
    }

    public void svcMemberDelete(Long id) {
        memberRepository.deleteById(id);
    }

    @Override
    public Optional<MemberInfoDTO> svcGetMemberInfo(Long userSeq) {
        return memberRepository.myfindUserInfo(userSeq);
    }
}
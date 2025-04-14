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
    
    // 유저 정보 출력 - 경준
    @Override
    public Optional<MemberInfoDTO> svcGetMemberInfo(Long userSeq) {
        return memberRepository.myfindUserInfo(userSeq);
    }
    
    // 유저 비밀번호 변경 - 경준
    @Override
    public boolean svcChangePassword(Long userSeq, String oldPassword, String newPassword) {
            // 유저 조회
            MemberEntity user = memberRepository.findByUserSeq(userSeq);

            if (user == null) {
                throw new RuntimeException("유저를 찾을 수 없습니다.");
            }

            // 기존 비밀번호 검증
            if (!user.getUserpw().equals(oldPassword)) {
                throw new RuntimeException("기존 비밀번호가 일치하지 않습니다.");
            }

            // 새 비밀번호 설정 및 저장
            user.setUserpw(newPassword);
            memberRepository.save(user);

            return true;
    }
}

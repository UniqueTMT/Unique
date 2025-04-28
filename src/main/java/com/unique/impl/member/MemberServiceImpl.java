package com.unique.impl.member;

import com.unique.dto.member.MemberInfoDTO;
import com.unique.entity.member.MemberEntity;
import com.unique.repository.member.MemberRepository;
import com.unique.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    /**
     * 사용자 이름과 이메일을 기반으로 userId(학번)를 조회하고 마스킹 처리하여 반환
     * <p>
     * 예시: userId가 "20140293"일 경우 → "20****93"으로 반환
     * </p>
     *
     * @param username 사용자 이름
     * @param email 사용자 이메일
     * @return 마스킹된 userId 문자열, 일치하는 사용자가 없으면 null
     */
    @Override
    public String svcFindUserIdByInfo(String username, String email) {
        Optional<MemberEntity> memberEntity = memberRepository.findByUsernameAndEmail(username, email);

        if (memberEntity.isEmpty()) return null;

        Long userId = memberEntity.get().getUserid();   // DB에서 조회한 사용자 학번
        String idStr = String.valueOf(userId);          // 마스킹 처리를 위한 타입변환

        if (idStr.length() >= 6) {
            return idStr.substring(0, 2) + "****" + idStr.substring(idStr.length() - 2);
        } else {
            return "****"; // 너무 짧은 ID인 경우 최소한의 보호 처리
        }
    }


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
    public boolean svcChangePassword(Long userSeq, Long userid, String oldPassword, String newPassword) {
            // 유저 조회
            MemberEntity user = memberRepository.findByUserSeq(userSeq);

            if (user == null) {
                throw new RuntimeException("유저를 찾을 수 없습니다.");
            }
            // 유저 아이디 검증 (userid 먼저!)
            if (!user.getUserid().equals(userid)) {
                throw new RuntimeException("유저 아이디가 일치하지 않습니다.");
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

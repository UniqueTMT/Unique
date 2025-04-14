package com.unique.service.member;
import com.unique.dto.member.MemberInfoDTO;
import com.unique.entity.member.MemberEntity;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    List<MemberEntity> svcMemberList();
    Optional<MemberEntity> svcMemberDetail(Long id);
    void svcMemberInsert(MemberEntity entity);
    void svcMemberUpdate(MemberEntity entity);
    void svcMemberDelete(Long id);
    
    // 유저 정보 출력 - 경준
    Optional<MemberInfoDTO> svcGetMemberInfo(Long id);
    
    // 유저 비밀번호 변경 - 경준
    boolean svcChangePassword(Long userSeq,String oldPassword,String newPassword);
}

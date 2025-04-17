package com.unique.service.member;
import com.unique.dto.member.MemberInfoDTO;
import com.unique.entity.member.MemberEntity;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    /**
     * 사용자 이름과 이메일을 기준으로 userId(학번)를 찾아 마스킹 처리된 값을 반환하는 서비스 메서드
     * @param username
     * @param email
     * @return 마스킹 처리된 userId (예: "20****93"), 없을 경우 null
     */
    String svcFindUserIdByInfo(String username, String email);

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

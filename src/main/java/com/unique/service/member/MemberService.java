package com.unique.service.member;
import com.unique.dto.member.MemberInfoDTO;
import com.unique.entity.member.MemberEntity;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    /**
     * 사용자 이름과 이메일을 기준으로 userId(학번)를 찾아 마스킹 처리된 값을 반환하는 서비스 메서드
     * 사용시기 : 아이디 찾기
     * @param username
     * @param email
     * @return 마스킹 처리된 userId (예: "20****93"), 없을 경우 null
     */
    String svcFindUserIdByInfo(String username, String email);

    /**
     * 사용자의 정보가 일치하면 임시 비밀번호를 생성 후 이메일로 임시 비밀번호를 전송하고 DB 비밀번호를 재설정
     * 사용시기 : 비밀번호 찾기
     * @param userId
     * @param username
     * @param email
     * @return true : 비밀번호 재설정 및 이메일 발송이 성공한 경우, false 또는 예외 발생 시 실패
     */
    boolean svcFindAndResetPassword(Long userId, String username, String email);


    List<MemberEntity> svcMemberList();
    Optional<MemberEntity> svcMemberDetail(Long id);
    void svcMemberInsert(MemberEntity entity);
    void svcMemberUpdate(MemberEntity entity);
    void svcMemberDelete(Long id);
    
    // 유저 정보 출력 - 경준
    Optional<MemberInfoDTO> svcGetMemberInfo(Long id);
    
    // 유저 비밀번호 변경 - 경준
    boolean svcChangePassword(Long userSeq,Long userid, String oldPassword,String newPassword);
}

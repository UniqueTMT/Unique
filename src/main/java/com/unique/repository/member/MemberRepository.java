package com.unique.repository.member;

import com.unique.dto.member.MemberInfoDTO;
import com.unique.entity.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    /**
     * 유저 정보 - 경준
     * @EntityGraph로 exam, exam.member 즉시 로딩
     */
    @Query("""
    SELECT new com.unique.dto.member.MemberInfoDTO(
        m.userSeq,
        m.userid,
        m.username,
        m.email,
        m.nickname,
        m.regdate
    )
    FROM MemberEntity m
    WHERE m.userSeq = :userSeq
""")
    Optional<MemberInfoDTO> myfindUserInfo(@Param("userSeq") Long userSeq);

    MemberEntity findByUserSeq(Long userSeq);


    /**
     * 사용자 학번으로 단건 조회하는 쿼리 메서드 정의
     * 사용시기 : Security의 CustomUserDetailService에서 로그인하려는 사용자 학번이 DB에 존재하는지 확인하기 위해
     * 작성자 : 이제무
     * @param userId
     * @return Optional<MemberEntity>
     */
    Optional<MemberEntity> findByUserid(Long userId);

    /**
     * 사용자 이름과 이메일을 기준으로 회원 정보를 조회
     * 사용시기 : 아이디 찾기
     * @param username
     * @param email
     * @return Optional<MemberEntity> 해당 조건에 일치하는 사용자 정보
     */
    Optional<MemberEntity> findByUsernameAndEmail(String username, String email);

    /**
     * 사용자 학번, 이름 및 이메일을 기준으로 회원 정보를 조회
     * 사용시기 : 비밀번호 찾기
     * @param userId
     * @param username
     * @param email
     * @return Optional<MemberEntity> 해당 조건에 일치하는 회원 정보
     */
    Optional<MemberEntity> findByUseridAndUsernameAndEmail(Long userId, String username, String email);

}
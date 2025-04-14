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
    SELECT new com.unique.dto.MemberInfoDTO(
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

}
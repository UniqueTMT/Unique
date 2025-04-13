package com.unique.repository;

import com.unique.dto.MemberInfoDTO;
import com.unique.entity.MemberEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {


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

}
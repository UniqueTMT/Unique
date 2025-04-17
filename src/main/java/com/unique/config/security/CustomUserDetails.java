package com.unique.config.security;

import com.unique.entity.member.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * [CustomUserDetails]
 * - 설명 : Spring Security가 인증 완료 후 사용자 정보를 보관하는 객체
 * - 구현 : UserDetails 인터페이스를 직접 구현하여 사용자 정보 제공
 * - 역할 : 로그인 이후 SecurityContext에 저장되며, 인증된 사용자 정보로 활용됨
 */

@RequiredArgsConstructor // final 필드를 위한 생성자 자동 생성
public class CustomUserDetails implements UserDetails {

    // DB에서 조회한 사용자 정보(MemberEntity)를 저장
    private final MemberEntity member;

    /**
     * [권한 정보 반환]
     * - ROLE_ 접두어를 붙여 Spring Security 권한 형식에 맞게 반환
     * - 예: "ADMIN" → "ROLE_ADMIN"
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + member.getRole().toUpperCase()));
    }

    /**
     * [비밀번호 반환]
     * - 인증 시 내부적으로 PasswordEncoder.matches()에서 사용
     */
    @Override
    public String getPassword() {
        return member.getUserpw();
    }

    /**
     * [사용자 이름 반환]
     * - 여기서는 학번(userId)을 사용자 ID로 사용
     * - 인증 이후 SecurityContextHolder.getContext().getAuthentication().getName()의 값이 됨
     */
    @Override
    public String getUsername() {
        return String.valueOf(member.getUserid());
    }

    /**
     * [계정 만료 여부]
     * - true = 만료되지 않음
     * - 여유를 위해 항상 true 반환
     */
    @Override
    public boolean isAccountNonExpired() { return true; }

    /**
     * [계정 잠김 여부]
     * - true = 잠기지 않음
     */
    @Override
    public boolean isAccountNonLocked() { return true; }

    /**
     * [비밀번호 만료 여부]
     * - true = 비밀번호 사용 가능
     */
    @Override
    public boolean isCredentialsNonExpired() { return true; }

    /**
     * [계정 활성화 여부]
     * - true = 활성화된 사용자
     * - MemberEntity의 active 필드와 연동해도 좋음
     */
    @Override
    public boolean isEnabled() { return true; }

    /**
     * [직접 사용자 정보를 가져오고 싶을 때 사용]
     * - 컨트롤러에서 CustomUserDetails로 캐스팅하여 member 정보 활용 가능
     */
    public MemberEntity getMember() { return member; }
}

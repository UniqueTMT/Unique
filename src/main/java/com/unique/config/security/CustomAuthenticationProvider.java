package com.unique.config.security;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * [CustomAuthenticationProvider]
 * - Spring Security에서 인증(Authentication)을 담당하는 핵심 클래스
 * - DB에서 사용자 정보를 조회하고, 비밀번호를 비교하고, 인증 토큰을 생성
 * - 직접 AuthenticationProvider를 구현하여 인증 로직을 커스터마이징
 */
@Component                  // 스프링 빈으로 등록
@RequiredArgsConstructor    // 생성자 주입
public class CustomAuthenticationProvider implements AuthenticationProvider {

    // 사용자 정보를 DB에서 불러오는 서비스
    private final CustomUserDetailsService userDetailsService;

    // 비밀번호 암호화/비교용 도구 (BCrypt)
    private final PasswordEncoder passwordEncoder;

    /**
     * [인증 메서드]
     * - 사용자로부터 입력 받은 아이디/비밀번호를 검증하고,
     * - 인증에 성공하면 Authentication 객체를 반환
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 유저한테 입력 받은 학번(아이디), 비밀번호
        String userId = authentication.getName();
        String userPw = (String) authentication.getCredentials();

        // CustomUserDetailsService를 통해 DB에서 사용자 정보 조회
        CustomUserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(userId);

        // DB에 저장된 암호화된 비밀번호
        String encodedPassword = userDetails.getPassword();

        // 입력한 비밀번호와 암호화된 비밀번호가 일치하는지 확인
        if (!passwordEncoder.matches(userPw, encodedPassword)) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // 인증 성공 시: 인증된 사용자 정보를 담은 토큰 생성
        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), // 아이디 (학번)
                null,                      // 비밀번호는 null로 설정
                userDetails.getAuthorities() // 사용자 권한 목록
        );

    }

    /**
     * [지원 여부 설정]
     * - 현재 이 Provider가 어떤 인증 토큰을 처리할 수 있는지를 지정
     * - 여기서는 UsernamePasswordAuthenticationToken 타입만 처리
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

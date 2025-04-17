package com.unique.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;

import java.util.List;

/**
 * [CustomAuthenticationManager]
 * - 설명 : CustomAuthenticationProvider를 명시적으로 등록하여 Spring Security의 인증 관리자를 설정하는 별도 구성 클래스
 * - 이유 : SecurityConfigDisable과의 순환 참조 방지
 * - 장점 : 구조 분리로 명확한 책임 분산 및 유지보수 용이성 확보
 */

@Configuration              // Spring 설정 클래스
@RequiredArgsConstructor    // 생성자 주입
public class CustomAuthenticationManager {

    private final CustomAuthenticationProvider customAuthenticationProvider;

    /**
     * [AuthenticationManager Bean 등록]
     * - 커스텀한 CustomAuthenticationProvider만 사용하는 ProviderManager를 등록
     * - Spring Security는 이 AuthenticationManager를 사용하여 실제 인증을 위임함
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        // ProviderManager는 여러 Provider를 가질 수 있지만, 여기선 하나만 명시
        return new ProviderManager(List.of(customAuthenticationProvider));
    }

}

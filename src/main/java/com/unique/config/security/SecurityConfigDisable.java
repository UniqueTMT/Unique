package com.unique.config.security;

// Lombok을 통해 생성자 자동 주입 (final 필드용)
import lombok.RequiredArgsConstructor;

// Spring Security 설정 클래스임을 명시
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * [Config]
 * - 설명 : REST API 방식 로그인 환경을 위한 Spring Security 설정 클래스
 * - 특징 : formLogin()을 비활성화하고, JSON 요청 기반으로 로그인 처리
 * - 사용시기 : exBuilder6, React 등 프론트에서 REST 방식으로 로그인 구현할 때 사용함.
 */
//@Configuration      // 스프링 설정 클래스로 등록
@EnableWebSecurity  // Spring Security 활성화
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true) // @PreAuthorize, @Secured 등 메서드 단위 권한 부여 허용
@RequiredArgsConstructor
public class SecurityConfigDisable {

    // 로그아웃 핸들러 주입
    private final CustomLogoutSuccessHandler logoutSuccessHandler;

    // 인증되지 않은 사용자가 /api/member/route에 접근할때 메세지 출력
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    // SecurityFilterChain 설정 : Spring Security의 핵심 필터 체인 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보안 토큰 비활성화 : REST API에서는 보통 비활성화함
                .csrf(csrf -> csrf.disable())

                // 🔥 EntryPoint 설정 추가
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )

                // 권한별 URL 접근 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/member/login").permitAll()         // 로그인 API는 누구나 접근 가능
                        .requestMatchers("/api/member/route").authenticated()     // 로그인 후 권한 분기 (인증된 사용자만)

                        .requestMatchers("/api/admin/**").hasRole("ADMIN")        // ADMIN 전용
                        .requestMatchers("/api/**").hasAnyRole("STUDENT", "PROFESSOR")  // 유저 전용
                        .anyRequest().denyAll()                                     // 그 외 요청은 모두 제한
                )

                // formLogin() 비활성화 : REST 방식에서는 리다이렉션 기반 로그인 사용하지 않기 때문
                .formLogin(form -> form.disable())    // ✅ REST 방식에서는 비활성화

                // 로그아웃 응답을 JSON 메시지로 커스터마이징
                .logout(logout -> logout
                        .logoutUrl("/api/member/logout")
                        .logoutSuccessHandler(logoutSuccessHandler) // ✅ 적용됨!
                        .invalidateHttpSession(true)
                );


        // 설정된 http 보안 필터 체인 반환
        return http.build();
    }

    // 로그인 비밀번호 암호화/비교용을 위한 PasswordEncoder 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();     // Bcrypt 방식 암호화 사용
    }

}
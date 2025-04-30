package com.unique.config.security;

// Lombok을 통해 생성자 자동 주입 (final 필드용)
import lombok.RequiredArgsConstructor;

// Spring Security 설정 클래스임을 명시
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


/**
 * 설명 : REST API 로그인 환경을 위한 Spring Security 설정 클래스
 *
 * <p>주요 기능:
 * <ul>
 *   <li>CORS 설정 (ExBuilder6 UI 호스트 허용)</li>
 *   <li>CSRF 비활성화</li>
 *   <li>인증 예외 처리 → JSON 401 응답</li>
 *   <li>URL 권한별 접근 제어</li>
 *   <li>formLogin 비활성화</li>
 *   <li>커스텀 로그아웃 핸들러 등록</li>
 * </ul>
 * </p>
 */
@Configuration      // 스프링 설정 클래스로 등록
@EnableWebSecurity  // Spring Security 활성화
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true) // @PreAuthorize, @Secured 등 메서드 단위 권한 부여 허용
@RequiredArgsConstructor
public class SecurityConfigDisable {

    // 로그아웃 성공 시 JSON 메시지를 반환하는 핸들러
    private final CustomLogoutSuccessHandler logoutSuccessHandler;

    // 사용자가 인증 실패(401) 시 JSON 메시지를 반환하는 엔트리 포인트
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    /**
     * eXBuilder6 UI(로컬 개발 서버)에서 오는 AJAX 요청을 허용하는 CORS 설정
     * <p>
     * eXBuilder6 개발 서버 또는 미리보기에서 띄운 UI 호스트(예: http://127.0.0.1:52194,
     * http://localhost:52194)에서 오는 AJAX 요청을 허용
     * </p>
     *
     * @return {@link CorsConfigurationSource}
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowCredentials(true);
        // 실제 브라우저 주소창에 찍히는 호스트:포트 명시
        cfg.setAllowedOriginPatterns(List.of(
            "http://127.0.0.1:52194",
            "http://localhost:52194",
            "*"
        ));
        cfg.setAllowedHeaders(List.of(CorsConfiguration.ALL));
        // JSON 로그인 요청 및 기타 REST 호출, 프리플라이트(OPTIONS) 허용
        cfg.setAllowedMethods(List.of(
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.DELETE.name(),
            HttpMethod.OPTIONS.name()
        ));

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        // /api/** 로 시작하는 엔드포인트에만 이 CORS 정책 적용
        src.registerCorsConfiguration("/api/**", cfg);
        return src;
    }


    /**
     * Spring Security 필터 체인 설정.
     *
     * @param http {@link HttpSecurity}
     * @return 구성된 {@link SecurityFilterChain}
     * @throws Exception 설정 중 예외 발생 시
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS 람다 구성 → Deprecated 해결
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )

                // REST API 용으로 CSRF 보안 토큰 비활성화
                .csrf(csrf -> csrf.disable())

                // 인증 예외 처리(JSON 401)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )

                // 권한별 URL 접근 설정
                .authorizeHttpRequests(auth -> auth
                		
                		// (1) 정적 리소스 및 클라이언트 화면: 항상 허용
                        .requestMatchers(
                            "/ui/**",           // .clx 파일
                            "/resource/**",     // cleopatra.js, css, 이미지 등
                            "/static/**",       // static 디렉터리 (필요시)
                            "/favicon.ico",      // 파비콘
                            "/error"              // <-- 여기에 추가
                        ).permitAll()
                        
                        // 프리플라이트 요청 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        
                        .requestMatchers("/api/member/login").permitAll()         // 로그인 API는 누구나 접근 가능
                        .requestMatchers("/api/member/find-id").permitAll()       
                        .requestMatchers("/api/member//find-password").permitAll()
                        .requestMatchers("/api/member/route").authenticated()     // 로그인 후 권한 분기 (인증된 사용자만)

//                        .requestMatchers("/api/admin/**").hasRole("ADMIN")        // ADMIN 전용
//                        .requestMatchers("/api/**").hasAnyRole("STUDENT", "PROFESSOR")  // 유저 전용
//                        .anyRequest().denyAll()                                     // 그 외 요청은 모두 제한
                                .anyRequest().permitAll()
                )

                // formLogin() 비활성화 : REST 방식에서는 리다이렉션 기반 로그인 사용하지 않기 때문
                .formLogin(form -> form.disable())    // ✅ REST 방식에서는 비활성화

                // 로그아웃 핸들러 등록 : 응답을 JSON 메시지로 커스터마이징
                .logout(logout -> logout
                        .logoutUrl("/api/member/logout")
                        .logoutSuccessHandler(logoutSuccessHandler) // ✅ 적용됨!
                        .invalidateHttpSession(true)
                );

        // 설정된 http 보안 필터 체인 반환
        return http.build();
    }

    /**
     * 비밀번호 암호화/비교용 {@link PasswordEncoder} 빈 등록.
     *
     * @return {@link BCryptPasswordEncoder} 인스턴스
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();     // Bcrypt 방식 암호화 사용
    }

}
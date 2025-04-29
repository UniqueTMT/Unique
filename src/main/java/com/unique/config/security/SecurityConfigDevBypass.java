package com.unique.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 개발 환경용 “모든 요청 허용” Spring Security 설정 클래스
 *
 * <p>기본적으로 .authorizeHttpRequests().anyRequest().permitAll() 로
 * 모든 요청을 통과시키지만, 브라우저 → 백엔드 간 AJAX 호출을 위해
 * 반드시 CORS 설정을 추가해야 합니다.</p>
 */
//@Configuration
//@Configuration  // 실서비스 시에는 이 클래스가 아닌 SecurityConfigDisable 을 사용하세요
@EnableWebSecurity
public class SecurityConfigDevBypass implements WebMvcConfigurer {

    /**
     * CORS 전역 설정.
     * <p>ExBuilder6 런타임(예: http://127.0.0.1:52194) 에서 호출되는
     * /api/** 엔드포인트를 허용합니다.</p>
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("http://127.0.0.1:52194", "http://localhost:52194")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /**
     * SecurityFilterChain.
     * <p>모든 URL을 허용하며 CSRF, formLogin, httpBasic을 비활성화합니다.</p>
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 위 addCorsMappings() 로 처리되기 때문에 .cors() 호출만 해주면 됩니다.
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                // REST 방식이므로 폼 로그인·기본 인증 다 꺼둡니다.
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                // 모든 요청을 허가
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
        return http.build();
    }

    /**
     * 테스트용 빈이지만, 비밀번호 암호화가 필요한 경우를 대비해 남겨둡니다.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

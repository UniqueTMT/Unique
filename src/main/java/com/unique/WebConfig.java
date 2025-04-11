package com.unique;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // 모든 경로 허용
        .allowedOrigins("http://127.0.0.1:52194", "http://localhost:52194") // 프론트 주소 허용
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
        .exposedHeaders("Content-Disposition")
        .allowedHeaders("*") // 모든 헤더 허용
        .allowCredentials(true) // 쿠키 허용 (필요하면)
        .maxAge(3600); // 캐싱 시간
  }
}

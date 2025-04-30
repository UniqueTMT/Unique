//package com.unique.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//
//    @Configuration
//    public class CorsConfig {
//        @Bean
//        public CorsConfigurationSource corsConfigurationSource() {
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowedOrigins(Arrays.asList(
//                    "http://127.0.0.1:52194", "http://localhost:52194", "http://localhost:8081"
//            ));
//            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//            config.setAllowedHeaders(Arrays.asList("*"));
//            config.setExposedHeaders(Arrays.asList("Content-Disposition"));
//            config.setAllowCredentials(true);
//            config.setMaxAge(3600L);
//
//            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//            source.registerCorsConfiguration("/**", config);
//            return source;
//        }
//
//
//}

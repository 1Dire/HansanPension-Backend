package com.hansanpension.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    // SecurityFilterChain을 사용하여 HttpSecurity 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()  // CSRF 비활성화
                .authorizeHttpRequests()  // 이 부분을 수정해야 함
                .requestMatchers("/auth/kakao").permitAll()  // 카카오 로그인 API는 모두 접근 허용
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()  // Swagger UI 및 API 문서 접근 허용
                .requestMatchers("/api/**").permitAll() // API는 모두 허용
                .anyRequest().authenticated();  // 그 외 모든 요청은 인증 필요
        return http.build();
    }

    // CORS 필터 설정
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173");  // Vite 리액트 앱 주소
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

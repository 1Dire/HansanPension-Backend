package com.hansanpension.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 URL 패턴에 대해
                .allowedOrigins(
                        "http://61.75.57.247:5173", // ✅ 본인 IP 접속용
                        "http://hansanpension-1408501111.ap-northeast-2.elb.amazonaws.com" // ✅ ALB 주소 허용
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드
                .allowedHeaders("*")  // 모든 헤더 허용
                .allowCredentials(true);  // 쿠키나 인증 정보를 요청에 포함할 수 있도록 설정
    }
}

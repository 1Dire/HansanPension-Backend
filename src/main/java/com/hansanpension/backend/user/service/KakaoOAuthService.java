package com.hansanpension.backend.user.service;

import com.hansanpension.backend.user.entity.User;
import com.hansanpension.backend.user.repository.UserRepository;
import com.hansanpension.backend.security.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class KakaoOAuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public KakaoOAuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String kakaoLogin(String accessToken) {
        WebClient webClient = WebClient.create("https://kapi.kakao.com");

        Map<String, Object> response = webClient.get()
                .uri("/v2/user/me")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String kakaoId = String.valueOf(response.get("id"));

        // 이미 존재하는 사용자 찾기
        User user = userRepository.findByKakaoId(kakaoId).orElseGet(() -> {
            User newUser = new User();
            newUser.setKakaoId(kakaoId);
            newUser.setSignupDate(LocalDateTime.now());
            newUser.setRole(User.Role.USER); // 기본적으로 USER 역할 부여
            return newUser;
        });

        user.setLastLoginDate(LocalDateTime.now());
        userRepository.save(user);

        // 사용자의 역할에 따라 role 설정
        String role = user.getRole().toString();  // USER 또는 ADMIN

        // JWT 토큰 생성 (role 포함)
        return jwtTokenProvider.generateToken(kakaoId, role);
    }
}

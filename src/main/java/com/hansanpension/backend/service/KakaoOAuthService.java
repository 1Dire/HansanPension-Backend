package com.hansanpension.backend.service;

import com.hansanpension.backend.entity.User;
import com.hansanpension.backend.repository.UserRepository;
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

        User user = userRepository.findByKakaoId(kakaoId).orElseGet(() -> {
            User newUser = new User();
            newUser.setKakaoId(kakaoId);
            newUser.setSignupDate(LocalDateTime.now());
            return newUser;
        });

        user.setLastLoginDate(LocalDateTime.now());
        userRepository.save(user);

        return jwtTokenProvider.generateToken(kakaoId);
    }
}

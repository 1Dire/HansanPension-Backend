package com.hansanpension.backend.controller;

import com.hansanpension.backend.dto.KakaoLoginRequestDto;
import com.hansanpension.backend.service.KakaoOAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class KakaoAuthController {

    private final KakaoOAuthService kakaoOAuthService;

    public KakaoAuthController(KakaoOAuthService kakaoOAuthService) {
        this.kakaoOAuthService = kakaoOAuthService;
    }

    @PostMapping("/kakao")
    public ResponseEntity<Map<String, String>> kakaoLogin(@RequestBody KakaoLoginRequestDto request) {
        // JWT 토큰 생성
        String accessToken = kakaoOAuthService.kakaoLogin(request.getAccessToken());

        // 응답할 데이터 구성
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);  // JWT 토큰을 response에 담음

        // ResponseEntity를 사용하여 JSON 응답 반환
        return ResponseEntity.ok(response);
    }
}

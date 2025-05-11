package com.hansanpension.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // 256비트 이상의 키를 안전하게 생성
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String kakaoId) {
        return Jwts.builder()
                .setSubject(kakaoId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1일
                .signWith(SECRET_KEY) // secretKey를 Key 객체로 전달
                .compact();
    }
}

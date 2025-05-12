package com.hansanpension.backend.user.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kakaoId;

    private LocalDateTime lastLoginDate;

    private LocalDateTime signupDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; // 기본값은 USER로 설정

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setKakaoId(String kakaoId) {
        this.kakaoId = kakaoId;
    }

    public String getKakaoId() {
        return kakaoId;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setSignupDate(LocalDateTime signupDate) {
        this.signupDate = signupDate;
    }

    public LocalDateTime getSignupDate() {
        return signupDate;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public enum Role {
        ADMIN, USER;
    }
}

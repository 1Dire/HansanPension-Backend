package com.hansanpension.backend.season.controller;

import com.hansanpension.backend.season.dto.SeasonRequest;
import com.hansanpension.backend.season.dto.SeasonResponse;
import com.hansanpension.backend.season.entity.Season;
import com.hansanpension.backend.season.service.SeasonService;
import com.hansanpension.backend.security.JwtTokenProvider;
import com.hansanpension.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seasons")
public class SeasonController {

    private final SeasonService seasonService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeasonResponse createSeason(@RequestBody SeasonRequest request) {
        Season season = seasonService.createSeason(request);
        return SeasonResponse.fromEntity(season);
    }

    @GetMapping
    public List<SeasonResponse> getAllSeasons() {
        return seasonService.getAllSeasons();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSeason(@PathVariable Long id,
                             @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new SecurityException("관리자만 삭제할 수 있습니다.");
        }

        String token = authorizationHeader.replace("Bearer ", "");
        if (!jwtTokenProvider.validateToken(token)) {
            throw new SecurityException("유효하지 않은 토큰입니다.");
        }

        String kakaoId = jwtTokenProvider.getSubject(token);
        boolean isAdmin = userRepository.findByKakaoId(kakaoId)
                .map(user -> user.getRole() == com.hansanpension.backend.user.entity.User.Role.ADMIN)
                .orElse(false);

        if (!isAdmin) {
            throw new SecurityException("관리자 권한이 필요합니다.");
        }

        seasonService.deleteSeason(id);
    }
}

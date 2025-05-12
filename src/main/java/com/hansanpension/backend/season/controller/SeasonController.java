package com.hansanpension.backend.season.controller;

import com.hansanpension.backend.season.dto.SeasonRequest;
import com.hansanpension.backend.season.dto.SeasonResponse;
import com.hansanpension.backend.season.entity.Season;
import com.hansanpension.backend.season.service.SeasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seasons")
public class SeasonController {

    private final SeasonService seasonService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeasonResponse createSeason(@RequestBody SeasonRequest request) {
        Season season = seasonService.createSeason(request);
        return SeasonResponse.fromEntity(season);
    }
}

package com.hansanpension.backend.season.controller;

import com.hansanpension.backend.season.dto.SeasonTypeResponse;
import com.hansanpension.backend.season.service.SeasonTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seasons")
@RequiredArgsConstructor
public class SeasonTypeController {

    private final SeasonTypeService seasonTypeService;

    @GetMapping
    public List<SeasonTypeResponse> getAllSeasons() {
        return seasonTypeService.getAllSeasonTypes();
    }
}

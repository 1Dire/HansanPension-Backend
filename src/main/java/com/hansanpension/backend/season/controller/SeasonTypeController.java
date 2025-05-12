package com.hansanpension.backend.season.controller;

import com.hansanpension.backend.season.dto.SeasonTypeResponse;
import com.hansanpension.backend.season.service.SeasonTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seasons")
public class SeasonTypeController {

    private final SeasonTypeService seasonTypeService;

    @GetMapping("/season-types")
    public List<SeasonTypeResponse> getAllSeasonTypes() {
        return seasonTypeService.getAllSeasonTypes();
    }
}

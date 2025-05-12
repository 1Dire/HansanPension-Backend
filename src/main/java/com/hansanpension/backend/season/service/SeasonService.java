package com.hansanpension.backend.season.service;

import com.hansanpension.backend.season.dto.SeasonRequest;
import com.hansanpension.backend.season.entity.Season;
import com.hansanpension.backend.season.entity.SeasonType;
import com.hansanpension.backend.season.repository.SeasonRepository;
import com.hansanpension.backend.season.repository.SeasonTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeasonService {

    private final SeasonRepository seasonRepository;
    private final SeasonTypeRepository seasonTypeRepository;

    public Season createSeason(SeasonRequest request) {
        SeasonType type = seasonTypeRepository.findById(request.getSeasonTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid SeasonType ID"));

        // 🛠️ SeasonType의 이름/설명을 가져와서 Season에 주입
        String name = type.getName(); // 예: "봄 시즌"
        String description = type.getDescription(); // 예: "날씨가 따뜻한 기간"

        Season season = new Season(
                name,
                description,
                request.getStartDate(),
                request.getEndDate(),
                type
        );

        return seasonRepository.save(season);
    }
}

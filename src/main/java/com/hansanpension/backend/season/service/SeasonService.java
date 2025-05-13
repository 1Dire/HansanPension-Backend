package com.hansanpension.backend.season.service;

import com.hansanpension.backend.season.dto.SeasonRequest;
import com.hansanpension.backend.season.entity.Season;
import com.hansanpension.backend.season.entity.SeasonType;
import com.hansanpension.backend.season.repository.SeasonRepository;
import com.hansanpension.backend.season.repository.SeasonTypeRepository;
import com.hansanpension.backend.season.dto.SeasonResponse;
import java.util.List;
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


        String name = type.getName();
        String description = type.getDescription();

        Season season = new Season(
                name,
                description,
                request.getStartDate(),
                request.getEndDate(),
                type
        );

        return seasonRepository.save(season);
    }

    public List<SeasonResponse> getAllSeasons() {
        return seasonRepository.findAll().stream()
                .map(SeasonResponse::fromEntity)
                .toList();
    }

    public void deleteSeason(Long id) {
        if (!seasonRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 시즌입니다.");
        }
        seasonRepository.deleteById(id);
    }
}

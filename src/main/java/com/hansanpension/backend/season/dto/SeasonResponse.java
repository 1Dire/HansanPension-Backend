package com.hansanpension.backend.season.dto;

import com.hansanpension.backend.season.entity.Season;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class SeasonResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String seasonTypeName;
    private Integer seasonTypeId;
    public static SeasonResponse fromEntity(Season season) {
        return SeasonResponse.builder()
                .id(season.getId())
                .name(season.getName())
                .description(season.getDescription())
                .startDate(season.getStartDate())
                .endDate(season.getEndDate())
                .seasonTypeId(season.getSeasonType().getId())
                .seasonTypeName(season.getSeasonType().getName())
                .build();
    }
}

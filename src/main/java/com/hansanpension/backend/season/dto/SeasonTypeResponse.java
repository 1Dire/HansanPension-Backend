package com.hansanpension.backend.season.dto;

import com.hansanpension.backend.season.entity.SeasonType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeasonTypeResponse {
    private Integer id;
    private String name;
    private String description;

    public static SeasonTypeResponse fromEntity(SeasonType seasonType) {
        return SeasonTypeResponse.builder()
                .id(seasonType.getId())
                .name(seasonType.getName())
                .description(seasonType.getDescription())
                .build();
    }
}

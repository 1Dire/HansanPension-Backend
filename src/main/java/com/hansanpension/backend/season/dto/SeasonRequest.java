package com.hansanpension.backend.season.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SeasonRequest {
    private Integer seasonTypeId;
    private LocalDate startDate;
    private LocalDate endDate;
}

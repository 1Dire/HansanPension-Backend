package com.hansanpension.backend.book.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class BookCreateDTO {
    private Long roomId;
    private String roomName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer numPeople;
    private Integer totalPrice;
    private String status;
    private String name;
    private String memo;
    private String isCharcoalIncluded; // ✅ 추가
}

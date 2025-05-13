package com.hansanpension.backend.book.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class BookDTO {
    private Long id;
    private Long roomId;
    private String roomName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer numPeople;
    private Integer totalPrice;
    private String status;
    private LocalDateTime createdAt;
    private String phoneNumber;
    private String name;
    private String memo;
    private String isCharcoalIncluded;
}

package com.hansanpension.backend.book.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class BookDTO {
    private Long id;
    private Integer roomId;
    private String roomName; // 🆕 방 이름 추가
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer numPeople;
    private Integer totalPrice;
    private String status;
    private LocalDateTime createdAt;
}

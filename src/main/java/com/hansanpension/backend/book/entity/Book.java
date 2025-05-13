package com.hansanpension.backend.book.entity;

import com.hansanpension.backend.room.entity.Room;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id")
    private Integer roomId;

    private String kakaoId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    private Integer numPeople;

    private Integer totalPrice;

    private String status;

    private String phoneNumber;

    private String name;  // ✅ 예약자 이름 필드 추가

    private String memo;  // ✅ 메모 필드 추가

    private LocalDateTime createdAt;

    @Column(name = "is_charcoal_included", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private String isCharcoalIncluded; // "Y" 또는 "N"

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Room room;

    public String getName() {
        return this.name;
    }
}

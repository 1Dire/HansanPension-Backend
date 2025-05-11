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

    // roomId -> room_id로 매핑
    @Column(name = "room_id") // 실제 테이블의 컬럼명
    private Integer roomId;

    private String kakaoId;

    private LocalDate checkIn;
    private LocalDate checkOut;

    private Integer numPeople;
    private Integer totalPrice;

    private String status;

    private String phoneNumber;

    private LocalDateTime createdAt;

    // ManyToOne 관계 설정 (room 테이블과의 관계)
    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Room room;
}

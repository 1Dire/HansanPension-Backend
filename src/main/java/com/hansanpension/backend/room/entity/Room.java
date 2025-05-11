package com.hansanpension.backend.room.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int size;
    private int capacity;

    @Column(name = "additional_fee_per_person")
    private int additionalFeePerPerson;

    @Column(name = "price_off_peak")
    private int priceOffPeak;

    @Column(name = "price_peak")
    private int pricePeak;

    @Column(name = "price_shoulder")
    private int priceShoulder;

    @Column(name = "price_weekend")
    private int priceWeekend;
}

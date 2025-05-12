package com.hansanpension.backend.season.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "season_types")
public class SeasonType {

    @Id
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
}

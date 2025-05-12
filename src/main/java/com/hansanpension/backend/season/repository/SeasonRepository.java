package com.hansanpension.backend.season.repository;

import com.hansanpension.backend.season.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<Season, Long> {
}

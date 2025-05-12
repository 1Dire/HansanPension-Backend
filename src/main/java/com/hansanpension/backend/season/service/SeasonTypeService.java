package com.hansanpension.backend.season.service;

import com.hansanpension.backend.season.dto.SeasonTypeResponse;
import com.hansanpension.backend.season.repository.SeasonTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeasonTypeService {

    private final SeasonTypeRepository seasonTypeRepository;

    public List<SeasonTypeResponse> getAllSeasonTypes() {
        return seasonTypeRepository.findAll()
                .stream()
                .map(SeasonTypeResponse::fromEntity)
                .collect(Collectors.toList());
    }
}

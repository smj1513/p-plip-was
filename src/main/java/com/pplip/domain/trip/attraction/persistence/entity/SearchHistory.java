package com.pplip.domain.trip.attraction.persistence.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchHistory {
    private Long id;
    private Long userId;

    private String keyword;
    private LocalDate searchedAt;
}

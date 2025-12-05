package com.pplip.domain.trip.plan.persistence.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Plan {
    private Long id;
    private Long userId;
    private String title;

    private LocalDate startDate;
    private LocalDate endDate;

    private LocalDateTime createdAt;
}

package com.pplip.domain.trip.plan.persistence.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToDo {
    private Long id;
    private Long planId;
    private Long attractionId;

    private String description;
    private LocalDate willStartAt;
    private LocalDate willEndAt;

    private LocalDateTime createdAt;
}

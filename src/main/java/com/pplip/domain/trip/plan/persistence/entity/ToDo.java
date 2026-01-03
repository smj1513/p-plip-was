package com.pplip.domain.trip.plan.persistence.entity;

import ch.qos.logback.core.util.Loader;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ToDo {
    private Long id;
    private Long planId;
    private Long attractionId;
    private String title;

    private String description;
    private LocalDateTime willStartAt;
    private LocalDateTime willEndAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.pplip.domain.trip.review.persistence.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {
    private Long id;
    private Long authorId;
    private Long attractionId;

    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

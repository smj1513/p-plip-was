package com.pplip.domain.trip.review.usecase.model;

import com.pplip.domain.trip.review.api.request.ReviewRequest;
import com.pplip.domain.trip.review.persistence.entity.Review;

import java.time.LocalDateTime;

public class ReviewModel {
    private Long id;
    private Long authorId;
    private Long attractionId;

    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReviewModel(ReviewRequest.Post post, Long authorId, Long attractionId) {
        this.authorId = authorId;
        this.attractionId = attractionId;
        this.content = post.getContent();
        this.createdAt = LocalDateTime.now();
    }

    public Review toEntity() {
        return Review.builder()
                .authorId(authorId)
                .attractionId(attractionId)
                .content(content)
                .createdAt(createdAt)
                .build();
    }
}

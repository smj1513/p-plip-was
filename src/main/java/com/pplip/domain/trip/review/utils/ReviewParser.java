package com.pplip.domain.trip.review.utils;

import com.pplip.domain.trip.review.api.response.ReviewResponse;
import org.springframework.stereotype.Component;


@Component
public class ReviewParser {
    public ReviewResponse.Update detailResToUpdateRes(ReviewResponse.Detail detail) {
        return ReviewResponse.Update.builder()
                .id(detail.getId())
                .username(detail.getUsername())
                .content(detail.getContent())
                .authorId(detail.getAuthorId())
                .isAuthor(detail.isAuthor())
                .userProfileImage(detail.getUserProfileImage())
                .reviewImages(detail.getReviewImages())
                .createdAt(detail.getCreatedAt())
                .updatedAt(detail.getUpdatedAt())
                .build();
    }
}

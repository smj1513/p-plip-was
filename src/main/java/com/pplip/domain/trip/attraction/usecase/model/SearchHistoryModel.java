package com.pplip.domain.trip.attraction.usecase.model;

import com.pplip.domain.trip.attraction.api.request.AttractionRequest;
import com.pplip.domain.trip.attraction.api.request.SearchHistoryRequest;
import com.pplip.domain.trip.attraction.persistence.entity.SearchHistory;

import java.time.LocalDate;

public class SearchHistoryModel {
    private Long id;
    private Long userId;

    private String keyword;
    private LocalDate searchedAt;

    public SearchHistoryModel(AttractionRequest.Search post, Long userId) {
        this.userId = userId;
        this.keyword = post.getQuery();
        this.searchedAt = LocalDate.now();
    }

    public SearchHistory toEntity() {
        return SearchHistory.builder()
                .userId(userId)
                .keyword(keyword)
                .searchedAt(searchedAt)
                .build();
    }
}

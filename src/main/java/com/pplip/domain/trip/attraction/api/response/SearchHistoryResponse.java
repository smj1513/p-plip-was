package com.pplip.domain.trip.attraction.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class SearchHistoryResponse {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class History {
        private Long id;
        private String keyword;
        private LocalDate searchedAt;
    }
}

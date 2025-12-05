package com.pplip.domain.trip.plan.api.response;

import com.pplip.domain.file.api.response.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ToDoResponse {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Summary {
        private Long id;
        private Long planId;
        private Long attractionId;

        private String attractionTitle;
        private String attractionImage;

        private LocalDate willStartAt;
        private LocalDate willEndAt;

        private LocalDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Detail {
        private Long id;
        private Long planId;
        private Long attractionId;

        private String attractionTitle;
        private String attractionImage;

        private String description;
        private LocalDate willStartAt;
        private LocalDate willEndAt;

        private LocalDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Update {
        private Long id;
        private Long planId;
        private Long attractionId;

        private String attractionTitle;
        private String attractionImage;

        private String description;
        private LocalDate willStartAt;
        private LocalDate willEndAt;

        private LocalDateTime createdAt;
    }
}

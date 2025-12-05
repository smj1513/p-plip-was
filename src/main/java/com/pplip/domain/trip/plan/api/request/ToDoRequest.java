package com.pplip.domain.trip.plan.api.request;

import com.pplip.domain.file.api.response.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class ToDoRequest {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Post {
        private Long planId;
        private Long attractionId;

        private String description;
        private LocalDate willStartAt;
        private LocalDate willEndAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Update {
        private Long id;
        private Long planId;
        private Long attractionId;

        private String description;
        private LocalDate willStartAt;
        private LocalDate willEndAt;
    }
}

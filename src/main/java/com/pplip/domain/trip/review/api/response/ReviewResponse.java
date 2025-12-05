package com.pplip.domain.trip.review.api.response;

import com.pplip.domain.file.api.response.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewResponse {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Detail {
        private Long id;
        private Long authorId;
        private String username;
        private String content;
        private FileResponse userProfileImage;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<FileResponse> reviewImages;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Update {
        private Long id;
        private String username;
        private String content;
        private FileResponse userProfileImage;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<FileResponse> reviewImages;
    }
}

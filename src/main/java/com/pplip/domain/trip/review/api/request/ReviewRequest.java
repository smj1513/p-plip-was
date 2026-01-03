package com.pplip.domain.trip.review.api.request;

import com.pplip.domain.file.api.request.FileRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class ReviewRequest {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Post {
        private String content;
        @Builder.Default
        private List<Long> fileIds = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Update {
        private String content;
        @Builder.Default
        private List<FileRequest> files = new ArrayList<>();
    }
}

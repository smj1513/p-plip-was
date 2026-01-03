package com.pplip.domain.board.notice.api.request;

import com.pplip.domain.file.api.request.FileRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class NoticeRequest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Post {
        private String title;
        private String content;
        @Builder.Default
        private List<Long> imageIds = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Update {
        private String title;
        private String content;
        @Builder.Default
        private List<FileRequest> images = new ArrayList<>();
    }
}
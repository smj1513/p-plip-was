package com.pplip.domain.board.notice.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pplip.domain.file.api.response.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class NoticeResponse {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Summary {
        private Long id;
        private String title;
        private String authorName;
        private int viewCnt;

        @JsonIgnore
        private long authorId;
        private boolean isAuthor;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Detail {
        private Long id;
        private String title;
        private String authorName;
        private FileResponse authorImage;
        private String content;
        private int viewCnt;
        private List<FileResponse> noticeImages;

        @JsonIgnore
        private long authorId;
        private boolean isAuthor;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Update {
        private Long id;
        private String title;
        private String authorName;
        private FileResponse authorImage;
        private String content;
        private int viewCnt;

        @JsonIgnore
        private long authorId;
        private boolean isAuthor;

        private List<FileResponse> noticeImages;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}

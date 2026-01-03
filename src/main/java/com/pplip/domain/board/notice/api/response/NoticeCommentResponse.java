package com.pplip.domain.board.notice.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pplip.domain.file.api.response.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class NoticeCommentResponse {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Summary {
        private Long id;
        private String authorNickName;
        private FileResponse profileImage;
        private String content;

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
        private String authorNickName;
        private FileResponse profileImage;
        private String content;

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
        private String authorNickName;
        private FileResponse profileImage;
        private String content;

        @JsonIgnore
        private long authorId;
        private boolean isAuthor;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}

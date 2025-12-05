package com.pplip.domain.board.notice.api.response;

import com.pplip.domain.file.api.response.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        private String content;
        private int viewCnt;
        private List<FileResponse> noticeImages;
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
        private String content;
        private int viewCnt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}

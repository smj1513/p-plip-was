package com.pplip.domain.board.freeboard.api.response;

import com.pplip.domain.file.api.response.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class FreeBoardCommentResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Retrieve{
        private Long id;
        private String content;
        private String authorNickName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private FileResponse profileImage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private Long id;
        private String content;
        private String authorNickName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private FileResponse profileImage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        private Long id;
        private String content;
        private String authorNickName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private FileResponse profileImage;
    }
}

package com.pplip.domain.board.freeboard.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pplip.domain.file.api.response.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FreeBoardResponse {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardList{
        private long id;
        private String title;
        private String authorName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private boolean isAuthor;
        private int likeCnt;
        private int viewCnt;
        private int commentCnt;
        private FileResponse freeBoardImage;

        @JsonIgnore
        private long userId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post{
        private long id;
        private String title;
        private String content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {
        private long id;
        private String title;
        private String content;
        private String authorName;
        private FileResponse authorImage;
        @JsonIgnore
        private long userId;
        private boolean isAuthor;

        @Builder.Default
        private List<FileResponse> freeBoardImages = new ArrayList<>();

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private int likeCnt;
        private int viewCnt;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Remove {
        private long id;
        private String title;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardLike {
        private boolean isLike;
    }
}


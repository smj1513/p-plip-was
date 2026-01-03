package com.pplip.domain.board.notice.persistence.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeComment {
    private Long id;
    private Long noticeBoardId;
    private Long authorId;

    private String content;
    private boolean isRemoved;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
}

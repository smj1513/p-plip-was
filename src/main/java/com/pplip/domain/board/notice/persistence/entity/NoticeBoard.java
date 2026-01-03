package com.pplip.domain.board.notice.persistence.entity;

import com.pplip.domain.board.notice.api.request.NoticeRequest;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeBoard {
    private Long id;
    private Long authorId;

    private String title;
    private String content;
    private int viewCnt;
    private boolean isRemoved;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void update(NoticeRequest.Update update) {
        this.title = update.getTitle();
        this.content = update.getContent();
        this.updatedAt = LocalDateTime.now();
    }
}

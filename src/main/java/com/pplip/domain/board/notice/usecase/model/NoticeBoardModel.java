package com.pplip.domain.board.notice.usecase.model;

import com.pplip.domain.board.notice.api.request.NoticeRequest;
import com.pplip.domain.board.notice.persistence.entity.NoticeBoard;

import java.time.LocalDateTime;

public class NoticeBoardModel {
    private Long id;
    private Long authorId;

    private String title;
    private String content;
    private Integer viewCnt;
    private Boolean isRemoved;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public NoticeBoardModel(NoticeRequest.Post data, Long authorId) {
        this.authorId = authorId;
        this.title = data.getTitle();
        this.content = data.getContent();
        this.viewCnt = 0;
        this.isRemoved = false;
        this.createdAt = LocalDateTime.now();
    }

    public NoticeBoardModel(NoticeRequest.Update data, Long id) {
        this.id = id;
        this.title = data.getTitle();
        this.content = data.getContent();
        this.updatedAt = LocalDateTime.now();
    }

    public NoticeBoard toEntity() {
        return NoticeBoard.builder()
                .id(id)
                .authorId(authorId)
                .content(content)
                .title(title)
                .createdAt(createdAt)
                .viewCnt(viewCnt)
                .isRemoved(isRemoved)
                .build();
    }
}

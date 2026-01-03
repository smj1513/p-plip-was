package com.pplip.domain.board.notice.usecase.model;

import com.pplip.domain.board.notice.api.request.NoticeCommentRequest;
import com.pplip.domain.board.notice.persistence.entity.NoticeComment;

import java.time.LocalDateTime;

public class NoticeCommentModel {
    private Long id;
    private Long noticeBoardId;
    private Long authorId;

    private String content;
    private Boolean isRemoved;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public NoticeCommentModel(NoticeCommentRequest.Post post, Long boardId, Long authorId) {
        this.noticeBoardId = boardId;
        this.authorId = authorId;
        this.content = post.getContent();
        this.isRemoved = false;
        this.createdAt = LocalDateTime.now();
    }

    public NoticeCommentModel(NoticeCommentRequest.Update update, Long id, Long authorId) {
        this.id = id;
        this.content = update.getContent();
        this.authorId = authorId;
        this.updatedAt = LocalDateTime.now();
    }

    public NoticeComment toEntity() {
        return NoticeComment.builder()
                .id(id)
                .authorId(authorId)
                .noticeBoardId(noticeBoardId)
                .content(content)
                .isRemoved(isRemoved)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}

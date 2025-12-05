package com.pplip.domain.board.notice.persistence.entity;

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
}

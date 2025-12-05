package com.pplip.domain.board.freeboard.persistence.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreeBoard {
    private Long id;
    private Long authorId;
    private String title;
    private String content;

    private int viewCnt;
    private boolean isRemoved;

    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}

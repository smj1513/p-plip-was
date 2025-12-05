package com.pplip.domain.board.freeboard.persistence.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLikeBoard {
    private Long boardId;
    private Long userId;
}

package com.pplip.global.api.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DomainCode {
    USER(1),
    BOARD(2),
    COMMENT(3),
    AUTH(4),
    ATTRACTION(5),
    PLAN(6),
    FILE(7), BOARD_LIKE(8);
    private int value;
}

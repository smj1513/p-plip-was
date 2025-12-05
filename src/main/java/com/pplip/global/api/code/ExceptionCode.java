package com.pplip.global.api.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    EMPTY(0),
    NOT_FOUND(1),
    NOT_SUPPORT(2),
    UN_EXPECTED(5),
    EXPIRED(6),
    MALFORMED(7),
    INVALID(8), FAILURE(4);
    private int value;

}

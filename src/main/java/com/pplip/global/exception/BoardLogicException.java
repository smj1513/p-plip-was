package com.pplip.global.exception;

import com.pplip.global.api.code.ErrorCode;

public class BoardLogicException extends AbstractErrorException {
    public BoardLogicException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BoardLogicException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}

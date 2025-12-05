package com.pplip.global.exception;

import com.pplip.global.api.code.ErrorCode;

public class UnvalidEmailCodeException extends AbstractErrorException {
    public UnvalidEmailCodeException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public UnvalidEmailCodeException(ErrorCode errorCode) {
        super(errorCode);
    }
}

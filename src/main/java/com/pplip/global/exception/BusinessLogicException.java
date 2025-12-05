package com.pplip.global.exception;

import com.pplip.global.api.code.ErrorCode;

public class BusinessLogicException extends AbstractErrorException{
    public BusinessLogicException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessLogicException(ErrorCode errorCode) {
        super(errorCode);
    }
}

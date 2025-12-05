package com.pplip.global.exception;


import com.pplip.global.api.code.ErrorCode;
import lombok.Getter;

@Getter
public abstract class AbstractErrorException extends RuntimeException {
    protected final ErrorCode errorCode;
    protected Object body;
    protected String message;

    public AbstractErrorException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = null;
    }

    public AbstractErrorException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}

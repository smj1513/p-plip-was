package com.pplip.global.exception;

import com.pplip.global.api.code.ErrorCode;

public class FileException extends AbstractErrorException {
    public FileException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FileException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}

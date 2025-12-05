package com.pplip.global.exception;

import com.pplip.global.api.code.ErrorCode;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomAuthenticationException extends AbstractErrorException {

    public CustomAuthenticationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}

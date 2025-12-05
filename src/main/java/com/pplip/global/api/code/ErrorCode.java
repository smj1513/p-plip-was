package com.pplip.global.api.code;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND_ERROR(DomainCode.USER, ExceptionCode.NOT_FOUND, "USER_NOT_FOUND_ERROR"),
    INVALIDATED_USER_ERROR(DomainCode.USER, ExceptionCode.INVALID, "USER_INVALIDATED"),
    FILE_TYPE_NOT_SUPPORT(DomainCode.FILE, ExceptionCode.NOT_SUPPORT, "FILE_TYPE_NOT_SUPPORT"),
    FILE_PROCESS_FAILURE(DomainCode.FILE, ExceptionCode.FAILURE, "FILE_PROCESS_FAILURE"),
    FILE_NOT_FOUND(DomainCode.FILE, ExceptionCode.NOT_FOUND,"FILE_NOT_FOUND"),
    UN_EXPECTED_TOKEN_VALIDATION(DomainCode.AUTH, ExceptionCode.UN_EXPECTED, "UN_EXPECTED_TOKEN_VALIDATION"),
    TOKEN_EXPIRED(DomainCode.AUTH, ExceptionCode.EXPIRED, "토큰이 만료되었습니다."),
    TOKEN_MALFORMED(DomainCode.AUTH, ExceptionCode.MALFORMED, "토큰이 위조되었습니다"),
    TOKEN_INVALID_SIGNATURE(DomainCode.AUTH,  ExceptionCode.INVALID, "토큰 서명이 일치하지 않습니다"),
    TOKEN_EMPTY(DomainCode.AUTH, ExceptionCode.EMPTY, "토큰이 비어있습니다." ),
    EXPIRED_EMAIL_VALID_CODE(DomainCode.USER, ExceptionCode.EXPIRED, "인증 기한이 만료되었습니다"),
    INVALID_EMAIL_VALID_CODE(DomainCode.USER, ExceptionCode.INVALID, "인증 번호가 틀립니다.");


    private DomainCode domainCode;
    private ExceptionCode exceptionCode;
    @Getter
    private String defaultMessage;

    public int status() {
        return domainCode.getValue() * 100 + exceptionCode.getValue();
    }


}

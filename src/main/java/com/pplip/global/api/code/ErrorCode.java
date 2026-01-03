package com.pplip.global.api.code;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND_ERROR(DomainCode.USER, ExceptionCode.NOT_FOUND, "USER_NOT_FOUND_ERROR"),
    INVALIDATED_USER_ERROR(DomainCode.USER, ExceptionCode.INVALID, "USER_INVALIDATED"),
    FAIL_TO_SEND_EMAIL_ERROR(DomainCode.USER, ExceptionCode.FAILURE, "FAIL_TO_SEND_EMAIL_ERROR"),
  	FORBIDDEN(DomainCode.USER, ExceptionCode.FORBIDDEN, "USER_FORBIDDEN"),
    ALREADY_EXISTS_EMAIL(DomainCode.USER, ExceptionCode.CONFLICT, "EMAIL_ALREADY_EXISTS"),
    FILE_TYPE_NOT_SUPPORT(DomainCode.FILE, ExceptionCode.NOT_SUPPORT, "FILE_TYPE_NOT_SUPPORT"),
    FILE_PROCESS_FAILURE(DomainCode.FILE, ExceptionCode.FAILURE, "FILE_PROCESS_FAILURE"),
    FILE_NOT_FOUND(DomainCode.FILE, ExceptionCode.NOT_FOUND,"FILE_NOT_FOUND"),
    UN_EXPECTED_TOKEN_VALIDATION(DomainCode.AUTH, ExceptionCode.UN_EXPECTED, "UN_EXPECTED_TOKEN_VALIDATION"),
    TOKEN_EXPIRED(DomainCode.AUTH, ExceptionCode.EXPIRED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(DomainCode.AUTH, ExceptionCode.INVALID, "INVALID_TOKEN"),
    TOKEN_MALFORMED(DomainCode.AUTH, ExceptionCode.MALFORMED, "토큰이 위조되었습니다"),
    TOKEN_INVALID_SIGNATURE(DomainCode.AUTH,  ExceptionCode.INVALID, "토큰 서명이 일치하지 않습니다"),
    TOKEN_EMPTY(DomainCode.AUTH, ExceptionCode.EMPTY, "토큰이 비어있습니다." ),
    EXPIRED_EMAIL_VALID_CODE(DomainCode.USER, ExceptionCode.EXPIRED, "인증 기한이 만료되었습니다"),
    INVALID_EMAIL_VALID_CODE(DomainCode.USER, ExceptionCode.INVALID, "인증 번호가 틀립니다."),
    ALREADY_EXIST_NICKNAME(DomainCode.USER, ExceptionCode.CONFLICT, "이미 존재하는 닉네임입니다."),
    FAIL_TO_CREATE_BOARD(DomainCode.BOARD, ExceptionCode.FAILURE, "게시글 작성에 실패했습니다."),
    BOARD_NOT_FOUND_ERROR(DomainCode.BOARD, ExceptionCode.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    BOARD_NOT_UPDATE(DomainCode.BOARD, ExceptionCode.INVALID, "게시글이 바뀌지 않았습니다."),
    BOARD_FAIL_DELETE(DomainCode.BOARD, ExceptionCode.FAILURE, "게시글 삭제에 실패했습니다."),
    EXISTS_BOARD_LIKE(DomainCode.BOARD, ExceptionCode.CONFLICT, "이미 좋아요한 게시판입니다."),
    FAIL_TO_DELETE_LIKE(DomainCode.BOARD, ExceptionCode.FAILURE, "좋아요 삭제에 실패했습니다."),
    FAIL_TO_CREATE_LIKE(DomainCode.BOARD, ExceptionCode.FAILURE, "게시글 좋아요에 실패했습니다."),
    COMMENT_CREATE_ERROR(DomainCode.COMMENT, ExceptionCode.FAILURE, "댓글 작성에 실패했습니다."),
    COMMENT_NOT_FOUND(DomainCode.COMMENT, ExceptionCode.NOT_FOUND, "댓글을 찾지 못하였습니다."),
    COMMENT_DELETE_FAIL_ERROR(DomainCode.COMMENT, ExceptionCode.FAILURE, "댓글 삭제에 실패했습니다."),
    ATTRACTION_NOT_FOUND(DomainCode.ATTRACTION, ExceptionCode.NOT_FOUND, "장소를 찾지 못하였습니다."),
    FAIL_TO_CREATE_SEARCH_HISTORY(DomainCode.ATTRACTION, ExceptionCode.FAILURE, "검색 기록 작성에 실패했습니다."),
    FAIL_TO_DELETE_SEARCH_HISTORY(DomainCode.ATTRACTION, ExceptionCode.FAILURE, "검색 기록 삭제에 실패했습니다."),
    REVIEW_CREATE_FAILURE(DomainCode.ATTRACTION, ExceptionCode.FAILURE, "리뷰 작성에 실패했습니다."),
    REVIEW_NOT_FOUND(DomainCode.ATTRACTION, ExceptionCode.NOT_FOUND, "리뷰 조회에 실패했습니다."),
    REVIEW_FAIL_DELETE(DomainCode.ATTRACTION, ExceptionCode.FAILURE, "리뷰 삭제에 실패했습니다."),
    SIDO_GUGUN_NOT_FOUND_ERROR(DomainCode.ATTRACTION, ExceptionCode.NOT_FOUND, "존재하지 않는 시도 구군 코드입니다."),
    SIDO_GUGUN_INVALID_VALUE(DomainCode.ATTRACTION, ExceptionCode.INVALID, "유효하지 않는 시도 구군 코드입니다."),
    INVALID_INPUT(DomainCode.USER, ExceptionCode.INVALID, "입력값이 유효하지 않습니다."),
    PLAN_NOT_FOUND(DomainCode.PLAN, ExceptionCode.NOT_FOUND,"계획 정보를 찾지 못했습니다."),
    PLAN_PROCESS_FAIL(DomainCode.PLAN,  ExceptionCode.FAILURE,"계획 작업 중 오류가 발생하였습니다."),
    BOARD_LIKE_PROCESS_FAILURE(DomainCode.BOARD_LIKE, ExceptionCode.FAILURE , "게시판 좋아요 작업 중 오류가 발생하였습니다."),
    AI_SERVER_PROCESS_ERROR(DomainCode.ATTRACTION, ExceptionCode.INTERNAL_SERVER_ERROR,"추론 서버에서 오류가 발생하였습니다."),
	ATTRACTION_REQUEST_BLOCKING_ERROR(DomainCode.ATTRACTION, ExceptionCode.TOO_MANY_REQUEST, "요청이 너무 많습니다."),
    PLAN_REQUEST_BLOCKING_ERROR(DomainCode.PLAN, ExceptionCode.TOO_MANY_REQUEST, "요청이 너무 많습니다.");

    private DomainCode domainCode;
    private ExceptionCode exceptionCode;
    @Getter
    private String defaultMessage;

    public int status() {
        return domainCode.getValue() * 100 + exceptionCode.getValue();
    }


}

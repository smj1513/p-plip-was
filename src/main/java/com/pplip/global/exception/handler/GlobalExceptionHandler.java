package com.pplip.global.exception.handler;

import com.pplip.domain.auth.utils.SecurityUtils;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.cache.utils.CacheType;
import com.pplip.global.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	private final List<Cache> caches;

	@ExceptionHandler(CustomAuthenticationException.class)
	public CommonResponse<Void> authenticationException(CustomAuthenticationException e) {
		return CommonResponse.fail(e.getErrorCode(), e.getMessage());
	}

	@ExceptionHandler(UnvalidEmailCodeException.class)
	public CommonResponse<Void> unvalidEmailCodeException(UnvalidEmailCodeException e) {
		return CommonResponse.fail(e.getErrorCode(), e.getMessage());
	}

	@ExceptionHandler(BoardLogicException.class)
	public CommonResponse<Void> failToCreateBoardException(BoardLogicException e) {
		return CommonResponse.fail(e.getErrorCode(), e.getMessage());
	}

	@ExceptionHandler(FileException.class)
	public CommonResponse<Void> fileException(FileException e) {
		return CommonResponse.fail(e.getErrorCode(), e.getMessage());
	}

	@ExceptionHandler(BusinessLogicException.class)
	public CommonResponse<Void> businessLogException(BusinessLogicException e) {
		return CommonResponse.fail(e.getErrorCode(), e.getMessage());
	}

	@ExceptionHandler(AIServerErrorException.class)
	public CommonResponse<Void> aiServerHandle(AIServerErrorException e) {
		Long userId = SecurityUtils.getCurrentUser().getUserId();
		for(Cache cache : caches){
			if(cache.getName().equals(e.getCacheType().getName())){
				CacheType cacheType = e.getCacheType();
				if (cache.evictIfPresent(userId)) {
					log.info("AI Server Error 발생으로 {}에서 userId {} 캐시 삭제", cacheType.getName(), userId);
				} else {
					log.info("AI Server Error 발생으로 {}에서 userId {} 캐시 삭제 시도했으나 캐시 없음", cacheType.getName(), userId);
				}
			}
		}
		return CommonResponse.fail(e.getErrorCode(), e.getMessage());
	}

}

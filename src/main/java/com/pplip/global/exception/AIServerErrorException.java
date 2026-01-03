package com.pplip.global.exception;

import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.cache.utils.CacheType;
import lombok.Getter;

public class AIServerErrorException extends AbstractErrorException {

	@Getter
	private CacheType cacheType;

	public AIServerErrorException() {
		super(ErrorCode.AI_SERVER_PROCESS_ERROR);
	}

	public AIServerErrorException(ErrorCode errorCode, CacheType cacheType) {
		super(errorCode);
		this.cacheType = cacheType;
	}
}

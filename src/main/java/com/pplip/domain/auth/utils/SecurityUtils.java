package com.pplip.domain.auth.utils;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

	public static boolean isAnonymous() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication == null || authentication.getPrincipal() instanceof String;
	}

	public static Account getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getPrincipal() instanceof String) {
			throw new BusinessLogicException(ErrorCode.INVALIDATED_USER_ERROR, "인증된 사용자만 이용할 수 있습니다.");
		}
		return (Account) authentication.getPrincipal();
	}

	public static Long resolveUserId(UserDetails userDetails) {
		if (userDetails != null) {
			return ((Account) userDetails).getUserId();
		} else {
			throw new BusinessLogicException(ErrorCode.INVALIDATED_USER_ERROR, "사용자가 인증 정보가 존재하지 않습니다.");
		}
	}

}

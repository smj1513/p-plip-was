package com.pplip.domain.auth.usecase.impl;

import com.pplip.domain.auth.jwt.Jwt;
import com.pplip.domain.auth.jwt.JwtUtil;
import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.auth.usecase.AuthService;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.cache.usecase.RefreshTokenCacheService;
import com.pplip.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {

	private final JwtUtil jwtUtil;
	private final RefreshTokenCacheService cacheService;

	@Override
	public Jwt issueNewTokens(String refreshToken) {
		if (!cacheService.validateRefreshToken(refreshToken)) {
			log.info("로직 in");
			throw new BusinessLogicException(ErrorCode.INVALID_TOKEN, "토큰이 유효하지 않습니다.");
		}

		Account account = jwtUtil.resolveRefreshToken(refreshToken);

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());
		Jwt jwts = jwtUtil.generate(authToken);

		cacheService.removeRefreshToken(refreshToken);
		cacheService.saveRefreshToken(jwts.getRefreshToken(), account.getUserId());
		return jwts;
	}
}

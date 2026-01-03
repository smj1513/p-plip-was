package com.pplip.domain.auth.filter.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pplip.domain.auth.jwt.Jwt;
import com.pplip.domain.auth.jwt.JwtProperties;
import com.pplip.domain.auth.jwt.JwtUtil;
import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.cache.usecase.RefreshTokenCacheService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
	private final JwtUtil jwtUtil;
	private final ObjectMapper om;
	private final RefreshTokenCacheService cacheService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		Jwt generate = jwtUtil.generate(authentication);

		cacheService.saveRefreshToken(generate.getRefreshToken(), ((Account) authentication.getPrincipal()).getUserId());

		Cookie cookie = new Cookie("refreshToken", generate.getRefreshToken());
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setMaxAge((int) JwtProperties.REFRESH_TOKEN_EXPIRE_TIME);
		response.addCookie(cookie);
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.print(om.writeValueAsString(CommonResponse.success(
				SuccessCode.SUCCESS,
				Map.of("accessToken", generate.getAccessToken()),
				"로그인 성공"
		)));
		writer.flush();
	}

}

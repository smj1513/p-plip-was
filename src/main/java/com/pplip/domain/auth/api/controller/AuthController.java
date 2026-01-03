package com.pplip.domain.auth.api.controller;

import com.pplip.domain.auth.dto.LoginRequest;
import com.pplip.domain.auth.jwt.Jwt;
import com.pplip.domain.auth.jwt.JwtProperties;
import com.pplip.domain.auth.usecase.AuthService;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.docs.AuthDocsController;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/auth")
@Slf4j
public class AuthController implements AuthDocsController {
	private final AuthService authService;

	@PostMapping("/refresh")
	public CommonResponse<Map<String, String>> refreshAccessToken(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
		Jwt jwt = authService.issueNewTokens(refreshToken);
		Cookie cookie = new Cookie("refreshToken", jwt.getRefreshToken());
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		cookie.setMaxAge((int) JwtProperties.REFRESH_TOKEN_EXPIRE_TIME);
		response.addCookie(cookie);
		return CommonResponse.success(SuccessCode.CREATED, Map.of("accessToken", jwt.getAccessToken()));
	}

	@Operation(summary = "로그인", description = "ID/PW를 통해 JWT 토큰을 발급받습니다.")
	@PostMapping("/login")
	public void fakeLoginEndpoint(@RequestBody LoginRequest loginRequest) {
	}

}
package com.pplip.domain.auth.usecase;

import com.pplip.domain.auth.jwt.Jwt;

import java.util.Map;

public interface AuthService {
	Jwt issueNewTokens(String refreshToken);
}

package com.pplip.domain.auth.jwt;

import com.pplip.domain.auth.persistence.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProvider provider;
    private final JwtResolver resolver;

    public Jwt generate(Authentication authentication) {

        return provider.generate(authentication);
    }

    public Account resolveAccessToken(String authHeader) {

        return resolver.resolve(authHeader);
    }

    public Account resolveRefreshToken(String refreshToken){
        return resolver.parse(refreshToken, JwtProperties.REFRESH_TOKEN_TYPE);
    }
}

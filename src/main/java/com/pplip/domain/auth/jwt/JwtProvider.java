package com.pplip.domain.auth.jwt;

import com.pplip.domain.auth.persistence.entity.Account;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.pplip.domain.auth.jwt.JwtProperties.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${JWT_SECRET}")
    private String secret;

    public Jwt generate(Authentication authentication) {
        Account account = (Account) authentication.getPrincipal();
        String access = create(account, ACCESS_TOKEN_EXPIRE_TIME, ACCESS_TOKEN_TYPE);
        String refresh = create(account, REFRESH_TOKEN_EXPIRE_TIME, REFRESH_TOKEN_TYPE);

        return new Jwt(access, refresh);
    }

    private String create(Account account, long time, String tokenType) {

        Date expiredDate = new Date();
        expiredDate.setTime(expiredDate.getTime() + time);

        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .claim(USERID, account.getUserId())
                .claim(ROLE, account.getRole().name())
                .claim("tokenType",tokenType)
                .expiration(expiredDate)
                .issuedAt(new Date())
                .signWith(secretKey)
                .compact();
    }
}

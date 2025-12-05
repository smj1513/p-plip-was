package com.pplip.domain.auth.jwt;

import com.pplip.domain.auth.persistence.entity.Account;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
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
public class JwtProvider {

    @Value("${JWT_SECRET}")
    private String secret;

    public Jwt generate(Authentication authentication) {
        Account account = (Account) authentication.getPrincipal();
        String access = create(account, ACCESS_TOKEN_EXPIRE_TIME);
        String refresh = create(account, REFRESH_TOKEN_EXPIRE_TIME);

        return new Jwt(access, refresh);
    }

    private String create(Account account, long time) {

        Date expiredDate = new Date();
        expiredDate.setTime(expiredDate.getTime() + time);

        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .claim(USERID, account.getId())
                .claim(EMAIL, account.getEmail())
                .claim(ROLE, account.getRole().name())
                .expiration(expiredDate)
                .issuedAt(new Date())
                .signWith(secretKey)
                .compact();
    }
}

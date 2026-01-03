package com.pplip.domain.auth.provider;

import com.pplip.domain.auth.jwt.JwtUtil;
import com.pplip.domain.auth.persistence.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;

    /**
     * filter 단에서 Authentication의 principal에 token 적재.
     *
     * token을 resolve하여 account를 인증된 Authentication 객체로 내보냄.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String header = (String) authentication.getPrincipal();

        Account account = jwtUtil.resolveAccessToken(header);
        return new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

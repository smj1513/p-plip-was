package com.pplip.domain.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pplip.domain.auth.dto.LoginRequest;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.CustomAuthenticationException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

@Log4j2
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper om;

    public CustomLoginFilter(ObjectMapper om) {
        super();
        this.om = om;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginRequest loginRequest = null;
        try {
            loginRequest = om.readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            log.error("CustomLoginFilter IO Exception 발생");
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
        AuthenticationManager am = getAuthenticationManager();
        log.info("id:{}, pw:{}", loginRequest.getId(), loginRequest.getPassword());
        UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(loginRequest.getId(), loginRequest.getPassword());
        return am.authenticate(principal);
    }
}

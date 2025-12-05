package com.pplip.domain.auth.filter.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.api.response.CommonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper om;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json;charset=UTF-8");
        writer.print(
                om.writeValueAsString(CommonResponse.fail(ErrorCode.USER_NOT_FOUND_ERROR, "ID 혹은 PW가 잘못되었습니다."))
        );
    }
}

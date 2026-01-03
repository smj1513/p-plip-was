package com.pplip.domain.auth.filter.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.api.response.CommonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;

import static org.apache.commons.codec.CharEncoding.UTF_8;

@RequiredArgsConstructor
@Log4j2
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper om;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("로그인 실패 원인: {}", exception.getClass().getSimpleName());
        log.error("에러 메시지: {}", exception.getMessage());
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();

        writer.print(
                om.writeValueAsString(CommonResponse.fail(ErrorCode.USER_NOT_FOUND_ERROR, "ID 혹은 PW가 잘못되었습니다."))
        );
        writer.flush();
    }
}

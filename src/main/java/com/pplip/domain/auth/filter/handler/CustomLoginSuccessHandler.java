package com.pplip.domain.auth.filter.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pplip.domain.auth.jwt.Jwt;
import com.pplip.domain.auth.jwt.JwtUtil;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    private JwtUtil jwtUtil;
    private ObjectMapper om;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Jwt generate = jwtUtil.generate(authentication);
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json;charset=UTF-8");
        writer.print(om.writeValueAsString(CommonResponse.success(
                SuccessCode.SUCCESS,
                generate,
                "로그인 성공"
        )));
        writer.flush();
    }

}

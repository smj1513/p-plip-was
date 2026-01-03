package com.pplip.global.docs;

import com.pplip.domain.auth.dto.LoginRequest;
import com.pplip.domain.auth.jwt.Jwt;
import com.pplip.global.api.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;

import java.util.Map;

@Tag(name = "인증/인가 API", description = "인증/인가 API")
public interface AuthDocsController {

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 통해 JWT 토큰을 재발급받습니다.(브라우저 정책상 작동은 하지않음)")
    @ApiResponse(responseCode = "201", description = "생성")
    CommonResponse<Map<String, String>> refreshAccessToken(String refreshToken, HttpServletResponse response);

    @Operation(summary = "로그인", description = "ID/PW를 통해 JWT 토큰을 발급받습니다.")
    @ApiResponse(responseCode = "200", description = "성공. 응답 헤더의 Authorization, refreshToken 필드 확인")
    void fakeLoginEndpoint(LoginRequest loginRequest);
}
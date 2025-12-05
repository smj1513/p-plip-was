package com.pplip.global.docs;


import com.pplip.domain.user.api.request.UserRequest;
import com.pplip.domain.user.api.response.UserResponse;
import com.pplip.global.api.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 관련 API 명세를 정의하는 인터페이스
 */
@Tag(name = "User API", description = "사용자 API, 마이페이지, 회원가입 등")
public interface UserDocsController {

    /**
     * 이메일 중복 여부를 확인합니다.
     *
     * @param nickname 확인할 닉네임
     * @return 중복 확인 결과
     */
    @Operation(summary = "이메일 중복 확인")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<UserResponse.DupCheck> emailDupCheck(@RequestParam String nickname);

    /**
     * 회원가입을 처리합니다.
     *
     * @param join 회원가입 요청 정보
     * @return 처리 결과
     */
    @Operation(summary = "회원가입")
    @ApiResponse(responseCode = "201", description = "생성")
    public CommonResponse<Void> join(@RequestBody UserRequest.Join join);

    /**
     * 인증 이메일을 발송합니다.
     *
     * @param email 이메일 발송 요청 정보
     * @return 처리 결과
     */
    @Operation(summary = "인증 이메일 발송")
    @ApiResponse(responseCode = "201", description = "생성")
    public CommonResponse<Void> sendVerificationEmail(@RequestBody UserRequest.Email email) throws MessagingException;

    /**
     * 이메일 인증을 확인합니다.
     *
     * @param emailCheck 이메일 인증 요청 정보
     * @return 인증 확인 결과
     */
    @Operation(summary = "이메일 인증 확인")
    @ApiResponse(responseCode = "200", description = "성공")
    public CommonResponse<UserResponse.EmailCheck> verificationEmail(@RequestBody UserRequest.EmailCheck emailCheck);

}

package com.pplip.domain.user.api.controller;

import com.pplip.domain.user.api.request.UserRequest;
import com.pplip.domain.user.api.response.UserResponse;
import com.pplip.domain.user.persistence.service.EmailService;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.docs.UserDocsController;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 관련 API 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController implements UserDocsController {

    private final EmailService emailService;

    /**
     * 닉네임 중복 여부를 확인합니다.
     *
     * @param nickname 확인할 닉네임
     * @return 닉네임 중복 여부
     */
    @Override
    @GetMapping("/join/nickname-dupcheck")
    public CommonResponse<UserResponse.DupCheck> emailDupCheck(@RequestParam String nickname) {
        return CommonResponse.success(SuccessCode.SUCCESS, null);
    }

    /**
     * 회원가입을 처리합니다.
     *
     * @param join 회원가입 요청 정보
     * @return 생성 성공 응답
     */
    @Override
    @PostMapping("/join")
    public CommonResponse<Void> join(@RequestBody UserRequest.Join join) {
        return CommonResponse.success(SuccessCode.CREATED, null);
    }

    /**
     * 인증 이메일을 발송합니다.
     *
     * @param email 발송할 이메일 정보
     * @return 생성 성공 응답
     */
    @Override
    @PostMapping("/join/send-verification-email")
    public CommonResponse<Void> sendVerificationEmail(@RequestBody UserRequest.Email email) throws MessagingException {
        emailService.sendEmail(email);
        return CommonResponse.success(SuccessCode.CREATED, null);
    }

    /**
     * 이메일 인증 코드를 확인합니다.
     *
     * @param emailCheck 확인할 이메일 및 인증 코드
     * @return 이메일 인증 확인 결과
     */
    @Override
    @PostMapping("/join/verification-email")
    public CommonResponse<UserResponse.EmailCheck> verificationEmail(@RequestBody UserRequest.EmailCheck emailCheck) {
        emailService.validate(emailCheck);

        return CommonResponse.success(SuccessCode.SUCCESS, null);
    }

}

package com.pplip.domain.user.usecase;

import com.pplip.domain.user.api.request.UserRequest;
import com.pplip.domain.user.api.response.UserResponse;
import jakarta.mail.MessagingException;

/**
 * 이메일 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 */
public interface EmailService {

    void sendEmail(UserRequest.Email email) throws MessagingException;

    UserResponse.EmailCheck validate(UserRequest.EmailCheck emailCheck);
}

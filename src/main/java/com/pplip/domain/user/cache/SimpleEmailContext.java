package com.pplip.domain.user.cache;

import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SimpleEmailContext implements EmailValidator {

    private static final long EXPIRATION_PERIOD = 5;

    private static ConcurrentHashMap<String, EmailValidationInfo> context = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, String> validationTokenContext = new ConcurrentHashMap<>();

    @Override
    public boolean valid(String email) {
        EmailValidationInfo emailValidationInfo = context.get(email);

        if (emailValidationInfo == null) {
            throw new BusinessLogicException(ErrorCode.INVALID_EMAIL_VALID_CODE, "유효하지 않은 인증코드입니다.");
        }

        LocalDateTime current = LocalDateTime.now();
        LocalDateTime expiredAt = emailValidationInfo.getPublishedAt().plusMinutes(EXPIRATION_PERIOD);

        return current.isBefore(expiredAt);
    }

    @Override
    public void putValidInfo(String email, EmailValidationInfo info) {
        context.put(email, info);
    }

    @Override
    public boolean isMatching(String email, String code) {
        EmailValidationInfo info = context.get(email);

        if (info.getCode().equals(code)) {
            return true;
        }

        return false;
    }

    @Override
    public void deleteInfo(String email) {
        context.remove(email);
    }

    @Override
    public boolean validVerificationToken(String token, String email) {
        String result = validationTokenContext.get(token);
        return email.equals(result);
    }

    @Override
    public void putValidVerificationTokenInfo(String token, String email) {
        validationTokenContext.put(token, email);
    }

    @Override
    public void deleteValidationTokenInfo(String token) {
        validationTokenContext.remove(token);
    }
}

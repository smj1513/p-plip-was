package com.pplip.domain.user.cache;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SimpleEmailContext implements EmailValidator {

    private static final long EXPIRATION_PERIOD = 5;

    private static ConcurrentHashMap<String, EmailValidationInfo> context = new ConcurrentHashMap<>();

    @Override
    public boolean valid(String email) {
        EmailValidationInfo emailValidationInfo = context.get(email);
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
}

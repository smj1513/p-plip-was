package com.pplip.domain.user.persistence.service.impl;

import com.pplip.domain.user.api.request.UserRequest;
import com.pplip.domain.user.cache.EmailValidationInfo;
import com.pplip.domain.user.cache.EmailValidator;
import com.pplip.domain.user.util.EmailSender;
import com.pplip.domain.user.persistence.service.EmailService;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.UnvalidEmailCodeException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final EmailValidator validator;
    private final EmailSender emailSender;

    @Override
    public void sendEmail(UserRequest.Email to) throws MessagingException {
        String code = createNumber();

        emailSender.sendMail(to.getEmail(), code);
        validator.putValidInfo(to.getEmail(), new EmailValidationInfo(code, LocalDateTime.now()));
    }

    @Override
    public void validate(UserRequest.EmailCheck emailCheck) {
        if (!validator.valid(emailCheck.getEmail())) {
            throw new UnvalidEmailCodeException(ErrorCode.EXPIRED_EMAIL_VALID_CODE);
        }
        log.info("validated in time");
        if (!validator.isMatching(emailCheck.getEmail(), emailCheck.getCode())) {
            throw new UnvalidEmailCodeException(ErrorCode.INVALID_EMAIL_VALID_CODE);
        }
        log.info("validated in matching code");
        validator.deleteInfo(emailCheck.getEmail());
    }


    private String createNumber() {
        return String.valueOf((int) (Math.random() * 90000) + 100000);
    }
}

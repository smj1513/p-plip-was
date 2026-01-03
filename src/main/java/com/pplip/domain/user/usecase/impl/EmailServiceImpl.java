package com.pplip.domain.user.usecase.impl;

import com.pplip.domain.auth.persistence.dao.AccountDao;
import com.pplip.domain.user.api.request.UserRequest;
import com.pplip.domain.user.api.response.UserResponse;
import com.pplip.domain.user.cache.EmailValidationInfo;
import com.pplip.domain.user.cache.EmailValidator;
import com.pplip.domain.user.usecase.EmailService;
import com.pplip.domain.user.utils.EmailSender;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import com.pplip.global.exception.UnvalidEmailCodeException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

	private final EmailValidator validator;
	private final EmailSender emailSender;
	private final AccountDao accountDao;

	@Override
	public void sendEmail(UserRequest.Email to) throws MessagingException {
		String code = createNumber();
		if (accountDao.findByEmail(to.getEmail()).isPresent()){
			throw new BusinessLogicException(ErrorCode.ALREADY_EXISTS_EMAIL, "이미 가입된 이메일입니다.");
		}

		emailSender.sendMail(to.getEmail(), code);
		validator.putValidInfo(to.getEmail(), new EmailValidationInfo(code, LocalDateTime.now()));
	}

	@Override
	public UserResponse.EmailCheck validate(UserRequest.EmailCheck emailCheck) {
		if (!validator.valid(emailCheck.getEmail())) {
			throw new UnvalidEmailCodeException(ErrorCode.EXPIRED_EMAIL_VALID_CODE);
		}

		log.info("validated in time");

		if (!validator.isMatching(emailCheck.getEmail(), emailCheck.getCode())) {
			throw new UnvalidEmailCodeException(ErrorCode.INVALID_EMAIL_VALID_CODE);
		}

		log.info("validated in matching code");
		validator.deleteInfo(emailCheck.getEmail());
		String validationToken = UUID.randomUUID().toString();
		validator.putValidVerificationTokenInfo(validationToken, emailCheck.getEmail());
		validator.deleteValidationTokenInfo(validationToken);

		return UserResponse.EmailCheck.builder().isSuccess(true).verificationToken(validationToken).build();
	}


	private String createNumber() {
		return String.valueOf((int) (Math.random() * 90000) + 100000);
	}
}

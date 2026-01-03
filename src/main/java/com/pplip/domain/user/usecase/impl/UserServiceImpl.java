package com.pplip.domain.user.usecase.impl;

import com.pplip.domain.auth.persistence.dao.AccountDao;
import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.auth.persistence.entity.Role;
import com.pplip.domain.user.api.request.UserRequest;
import com.pplip.domain.user.api.response.UserResponse;
import com.pplip.domain.user.cache.EmailValidator;
import com.pplip.domain.user.persistence.dao.ProfileDao;
import com.pplip.domain.user.persistence.dao.UserDao;
import com.pplip.domain.user.persistence.entity.Profile;
import com.pplip.domain.user.persistence.entity.User;
import com.pplip.domain.user.usecase.UserService;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
	private final UserDao userDao;
	private final ProfileDao profileDao;
	private final EmailValidator emailValidator;
	private final PasswordEncoder passwordEncoder;
	private final AccountDao accountDao;

	@Override
	public UserResponse.DupCheck nicknameDupCheck(String nickname) {
		return UserResponse.DupCheck.builder().isDup(profileDao.existsNickname(nickname)).build();
	}

	@Override
	public Void join(UserRequest.Join join) {
		if (emailValidator.validVerificationToken(join.getValidationToken(), join.getEmail())) {
			throw new BusinessLogicException(ErrorCode.INVALID_EMAIL_VALID_CODE, "이메일 검증 코드가 유효하지 않습니다.");
		}
		if (join.getName() == null || join.getName().isEmpty() || join.getNickname() == null || join.getNickname().isEmpty()) {
			throw new BusinessLogicException(ErrorCode.INVALID_INPUT);
		}
		if (profileDao.existsNickname(join.getNickname())) {
			throw new BusinessLogicException(ErrorCode.ALREADY_EXIST_NICKNAME);
		}
		User user = User.builder().name(join.getName()).birth(join.getBirth()).build();
		userDao.insert(user);
		Profile profile = Profile.builder()
				.userId(user.getId())
				.nickname(join.getNickname())
				.description(join.getDescription())
				.build();
		profileDao.insert(profile);
		Account account = Account.builder()
				.userId(user.getId())
				.role(Role.USER)
				.password(passwordEncoder.encode(join.getPassword()))
				.email(join.getEmail())
				.createdAt(LocalDateTime.now()).build();
		accountDao.insert(account);
		return null;
	}
}

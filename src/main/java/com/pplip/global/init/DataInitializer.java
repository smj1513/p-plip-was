package com.pplip.global.init;

import com.pplip.domain.auth.persistence.dao.AccountDao;
import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.auth.persistence.entity.Role;
import com.pplip.domain.user.persistence.dao.ProfileDao;
import com.pplip.domain.user.persistence.dao.UserDao;
import com.pplip.domain.user.persistence.entity.Profile;
import com.pplip.domain.user.persistence.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Log4j2
public class DataInitializer {
	private final AccountDao accountDao;
	private final InitService initService;
	private final PasswordEncoder passwordEncoder;

	@Value("${user.admin-info.id}")
	private String email;
	@Value("${user.admin-info.pw}")
	private String pw;

	@PostConstruct
	public void init() {
		if (accountDao.count() > 0) {
			log.info("Account Data already exists.");
			return;
		}
		long userId = initService.init();
		Account account = Account.builder()
				.email(email)
				.password(passwordEncoder.encode(pw))
				.userId(userId)
				.role(Role.ADMIN)
				.createdAt(LocalDateTime.now())
				.build();
		accountDao.insert(account);
	}

	@Component
	@RequiredArgsConstructor
	@Transactional
	@Log4j2
	public static class InitService {
		private final UserDao userDao;
		private final ProfileDao profileDao;
		@Value("${user.admin-info.name}")
		private String name;


		public long init() {
			User user = User.builder().name(name).birth(LocalDate.now()).build();
			userDao.insert(user);
			Profile profile = Profile.builder().userId(user.getId()).description("관리자").nickname(name).build();
			profileDao.insert(profile);
			return user.getId();
		}
	}
}

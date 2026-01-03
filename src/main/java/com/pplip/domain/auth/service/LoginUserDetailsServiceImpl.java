package com.pplip.domain.auth.service;

import com.pplip.domain.auth.persistence.dao.AccountDao;
import com.pplip.domain.auth.persistence.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginUserDetailsServiceImpl implements UserDetailsService {

    private final AccountDao repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("ID 혹은 비밀번호가 잘못되었습니다."));
        log.info("user details service : {}, account : {}", username, account);
        log.info("pw : {}", account.getPassword());
        boolean matches = passwordEncoder.matches("ssafy1234!", account.getPassword());
        log.info("비밀번호 직접 매칭 테스트 결과: {}", matches);
        return account;
    }
}

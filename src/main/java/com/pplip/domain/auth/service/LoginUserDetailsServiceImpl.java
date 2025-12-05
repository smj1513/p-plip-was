package com.pplip.domain.auth.service;

import com.pplip.domain.auth.persistence.dao.AccountDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginUserDetailsServiceImpl implements UserDetailsService {

    // TODO : AccountRepository 구현
    private final AccountDao repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("ID 혹은 비밀번호가 잘못되었습니다."));
    }
}

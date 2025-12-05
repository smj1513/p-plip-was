package com.pplip.domain.auth.persistence.dao;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.auth.persistence.entity.Role;
import com.pplip.domain.user.persistence.dao.UserDao;
import com.pplip.domain.user.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountDaoTest {

    @Autowired
    AccountDao dao;

    @Autowired
    UserDao userDao;

    private Long userId;

    @BeforeEach
    void setUp() {
        User user = User.builder().name("testuser").birth(LocalDate.now()).build();
        userDao.insert(user);
        userId = user.getId();
    }

    @Test
    @DisplayName("성공: 이메일로 계정을 찾을 수 있다")
    void findByEmail_Success() {
        // given
        String testEmail = "test@example.com";
        Account account = Account.builder()
                .userId(userId)
                .email(testEmail)
                .password("password")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .passwordUpdatedAt(LocalDateTime.now())
                .build();
        dao.insert(account);

        // when
        Optional<Account> foundAccount = dao.findByEmail(testEmail);

        // then
        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get().getEmail()).isEqualTo(testEmail);
    }

    @Test
    @DisplayName("실패: 존재하지 않는 이메일로 조회 시 빈 Optional을 반환한다")
    void findByEmail_Fail_WhenEmailDoesNotExist() {
        // given
        String nonExistentEmail = "nonexistent@example.com";

        // when
        Optional<Account> foundAccount = dao.findByEmail(nonExistentEmail);

        // then
        assertThat(foundAccount).isNotPresent();
    }
}
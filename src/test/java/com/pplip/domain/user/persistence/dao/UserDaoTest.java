package com.pplip.domain.user.persistence.dao;

import com.pplip.domain.user.persistence.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@MybatisTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserDaoTest {

    @Autowired
    UserDao dao;

    @Test
    @DisplayName("성공: DB에 저장된 유저를 ID로 찾을 수 있다")
    void findById_ShouldFindUser_WhenUserExists() {
        // given
        User user = User.builder()
                .name("testUser")
                .birth(LocalDate.now())
                .build();
        dao.insert(user);

        // when
        Optional<User> foundUserOptional = dao.findById(user.getId());

        // then
        assertThat(foundUserOptional).isPresent();
        foundUserOptional.ifPresent(foundUser -> {
            assertThat(foundUser.getId()).isEqualTo(user.getId());
            assertThat(foundUser.getName()).isEqualTo(user.getName());
            assertThat(foundUser.getBirth()).isEqualTo(user.getBirth());
        });
    }

    @Test
    @DisplayName("성공: 존재하지 않는 ID로 조회 시 빈 Optional을 반환한다")
    void findById_ShouldReturnEmpty_WhenUserDoesNotExist() {
        // given
        long nonExistentId = 999L;

        // when
        Optional<User> foundUserOptional = dao.findById(nonExistentId);

        // then
        assertThat(foundUserOptional).isNotPresent();
    }

    @Test
    @DisplayName("성공: 새로운 유저를 DB에 저장할 수 있다")
    void insert_ShouldSaveUser() {
        // given
        User user = User.builder()
                .name("newUser")
                .birth(LocalDate.now())
                .build();

        // when
        dao.insert(user);

        // then
        Optional<User> foundUserOptional = dao.findById(user.getId());
        assertThat(foundUserOptional).isPresent();
        assertThat(foundUserOptional.get().getName()).isEqualTo("newUser");
    }

    @Test
    @DisplayName("실패: 필수 필드(name)가 null이면 DataIntegrityViolationException 예외가 발생한다")
    void insert_ShouldThrowException_WhenNameIsNull() {
        // given
        User user = User.builder()
                .birth(LocalDate.now())
                .build();

        // when & then
        assertThatThrownBy(() -> dao.insert(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}

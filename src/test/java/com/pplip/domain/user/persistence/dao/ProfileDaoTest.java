package com.pplip.domain.user.persistence.dao;

import com.pplip.domain.user.persistence.entity.Profile;
import com.pplip.domain.user.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@MybatisTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProfileDaoTest {

    @Autowired
    ProfileDao dao;

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
    @DisplayName("성공: 새 프로필을 저장한 후 사용자 ID로 조회할 수 있다")
    void insertAndFindByUserId_Success() {
        // given
        Profile profile = Profile.builder()
                .userId(userId)
                .nickname("test-nickname")
                .description("hello world")
                .build();
        dao.insert(profile);

        // when
        Optional<Profile> foundProfileOptional = dao.findByUserId(userId);

        // then
        assertThat(foundProfileOptional).isPresent();
        Profile foundProfile = foundProfileOptional.get();
        assertThat(foundProfile.getNickname()).isEqualTo("test-nickname");
        assertThat(foundProfile.getDescription()).isEqualTo("hello world");
    }

    @Test
    @DisplayName("실패: 프로필이 없는 사용자 ID로 조회 시 빈 Optional을 반환한다")
    void findByUserId_Fail_WhenProfileDoesNotExist() {
        // given
        long nonExistentUserId = 999L;

        // when
        Optional<Profile> byUserId = dao.findByUserId(nonExistentUserId);

        // then
        assertThat(byUserId).isNotPresent();
    }

}
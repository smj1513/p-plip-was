package com.pplip.domain.user.usecase.impl;

import com.pplip.domain.auth.persistence.dao.AccountDao;
import com.pplip.domain.user.api.request.UserRequest;
import com.pplip.domain.user.api.response.UserResponse;
import com.pplip.domain.user.cache.EmailValidator;
import com.pplip.domain.user.persistence.dao.ProfileDao;
import com.pplip.domain.user.persistence.dao.UserDao;
import com.pplip.domain.user.persistence.entity.User;
import com.pplip.global.exception.BusinessLogicException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDao userDao;

    @Mock
    private AccountDao accountDao;

    @Mock
    private ProfileDao profileDao;

    @Mock
    private EmailValidator emailValidator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName("닉네임 중복 확인 - 중복되지 않는 경우")
    @Test
    void nicknameDupCheck_notDuplicated() {
        // given
        String nickname = "newNickname";
        given(profileDao.existsNickname(nickname)).willReturn(false);

        // when
        UserResponse.DupCheck result = userService.nicknameDupCheck(nickname);

        // then
        assertThat(result.isDup()).isFalse();
    }

    @DisplayName("닉네임 중복 확인 - 중복되는 경우")
    @Test
    void nicknameDupCheck_duplicated() {
        // given
        String nickname = "existingNickname";
        given(profileDao.existsNickname(nickname)).willReturn(true);

        // when
        UserResponse.DupCheck result = userService.nicknameDupCheck(nickname);

        // then
        assertThat(result.isDup()).isTrue();
    }

    @DisplayName("회원가입 - 성공")
    @Test
    void join_success() {
        // given
        UserRequest.Join joinRequest = UserRequest.Join.builder()
                .name("testUser")
                .nickname("testNickname")
                .description("testDescription")
                .build();

        given(userDao.insert(any(User.class))).willReturn(1);

        // when
        userService.join(joinRequest);

        // then
        verify(userDao).insert(any(User.class));
        verify(profileDao).insert(any(com.pplip.domain.user.persistence.entity.Profile.class));
    }

    @DisplayName("닉네임 중복 확인 - null 입력")
    @Test
    void nicknameDupCheck_nullNickname() {
        // given
        String nickname = null;
        given(profileDao.existsNickname(nickname)).willReturn(false);

        // when
        UserResponse.DupCheck result = userService.nicknameDupCheck(nickname);

        // then
        assertThat(result.isDup()).isFalse();
    }

    @DisplayName("닉네임 중복 확인 - 빈 문자열 입력")
    @Test
    void nicknameDupCheck_emptyNickname() {
        // given
        String nickname = "";
        given(profileDao.existsNickname(nickname)).willReturn(false);

        // when
        UserResponse.DupCheck result = userService.nicknameDupCheck(nickname);

        // then
        assertThat(result.isDup()).isFalse();
    }

    @DisplayName("회원가입 - 실패 (닉네임 중복)")
    @Test
    void join_fail_nicknameDuplication() {
        // given
        UserRequest.Join joinRequest = UserRequest.Join.builder()
                .name("testUser")
                .nickname("existingNickname")
                .description("testDescription")
                .build();
        given(profileDao.existsNickname(joinRequest.getNickname())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.join(joinRequest))
                .isInstanceOf(BusinessLogicException.class);
    }

    @DisplayName("회원가입 - 실패 (이름이 null)")
    @Test
    void join_fail_nullName() {
        // given
        UserRequest.Join joinRequest = UserRequest.Join.builder()
                .name(null)
                .nickname("testNickname")
                .description("testDescription")
                .build();

        // when & then
        assertThatThrownBy(() -> userService.join(joinRequest))
                .isInstanceOf(BusinessLogicException.class);
    }

    @DisplayName("회원가입 - 실패 (닉네임이 null)")
    @Test
    void join_fail_nullNickname() {
        // given
        UserRequest.Join joinRequest = UserRequest.Join.builder()
                .name("testUser")
                .nickname(null)
                .description("testDescription")
                .build();

        // when & then
        assertThatThrownBy(() -> userService.join(joinRequest))
                .isInstanceOf(BusinessLogicException.class);
    }

    @DisplayName("회원가입 - 실패 (이름이 공백)")
    @Test
    void join_fail_emptyName() {
        // given
        UserRequest.Join joinRequest = UserRequest.Join.builder()
                .name("")
                .nickname("testNickname")
                .description("testDescription")
                .build();

        // when & then
        assertThatThrownBy(() -> userService.join(joinRequest))
                .isInstanceOf(BusinessLogicException.class);
    }

    @DisplayName("회원가입 - 실패 (닉네임이 공백)")
    @Test
    void join_fail_emptyNickname() {
        // given
        UserRequest.Join joinRequest = UserRequest.Join.builder()
                .name("testUser")
                .nickname("")
                .description("testDescription")
                .build();

        // when & then
        assertThatThrownBy(() -> userService.join(joinRequest))
                .isInstanceOf(BusinessLogicException.class);
    }
}

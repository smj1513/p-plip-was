package com.pplip.domain.user.usecase.impl;

import com.pplip.domain.auth.persistence.dao.AccountDao;
import com.pplip.domain.user.api.request.UserRequest;
import com.pplip.domain.user.cache.EmailValidator;
import com.pplip.domain.user.utils.EmailSender;
import com.pplip.global.exception.UnvalidEmailCodeException;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailServiceImpl 테스트")
class EmailServiceImplTest {

    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private EmailValidator validator;

    @Mock
    private EmailSender emailSender;

    @Mock
    private AccountDao accountDao;

    @Test
    @DisplayName("성공: 인증 이메일 발송")
    void sendEmail_Success() throws MessagingException {
        // Given
        String email = "test@example.com";
        UserRequest.Email user = UserRequest.Email.builder().email(email).build();

        doNothing().when(emailSender).sendMail(anyString(), anyString());
        doNothing().when(validator).putValidInfo(anyString(), any());

        // When
        emailService.sendEmail(user);

        // Then
        verify(emailSender, times(1)).sendMail(eq(email), anyString());
        verify(validator, times(1)).putValidInfo(eq(email), any());
    }

    @Test
    @DisplayName("성공: 이메일 인증 코드 검증")
    void validate_Success() {
        // Given
        String email = "test@example.com";
        String code = "123456";
        UserRequest.EmailCheck emailCheck = new UserRequest.EmailCheck(email, code);

        when(validator.valid(email)).thenReturn(true);
        when(validator.isMatching(email, code)).thenReturn(true);

        // When
        emailService.validate(emailCheck);

        // Then
        verify(validator, times(1)).deleteInfo(email);
    }

    @Test
    @DisplayName("실패: 만료된 인증 코드로 검증 시 예외 발생")
    void validate_Fail_ExpiredCode() {
        // Given
        String email = "test@example.com";
        String code = "123456";
        UserRequest.EmailCheck emailCheck = new UserRequest.EmailCheck(email, code);

        when(validator.valid(email)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> emailService.validate(emailCheck))
                .isInstanceOf(UnvalidEmailCodeException.class);
    }

    @Test
    @DisplayName("실패: 일치하지 않는 인증 코드로 검증 시 예외 발생")
    void validate_Fail_MismatchedCode() {
        // Given
        String email = "test@example.com";
        String code = "123456";
        UserRequest.EmailCheck emailCheck = new UserRequest.EmailCheck(email, code);

        when(validator.valid(email)).thenReturn(true);
        when(validator.isMatching(email, code)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> emailService.validate(emailCheck))
                .isInstanceOf(UnvalidEmailCodeException.class);
    }
}
package com.pplip.domain.user.utils;

import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class EmailSender {

    private final JavaMailSender sender;

    public void sendMail(String to, String code) {
        MimeMessage mimeMessage = sender.createMimeMessage();

        // 수신자, 제목, 본문 등 설정
        String subject = "[P plip] 이메일 인증번호";
        String body = "이메일 인증번호입니다. <br/> <h2>%s</h2>".formatted(code);

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true);

            // Email 전송
            sender.send(mimeMessage);
        } catch (MailSendException e) {
            throw new BusinessLogicException(ErrorCode.FAIL_TO_SEND_EMAIL_ERROR, "메일 전송에 실패했습니다. 유효한 이메일인지 확인부탁드립니다.");
        } catch (MessagingException e) {
            throw new BusinessLogicException(ErrorCode.FAIL_TO_SEND_EMAIL_ERROR, "메일 전송에 실패했습니다. 유효한 이메일인지 확인부탁드립니다.");
        }

    }
}

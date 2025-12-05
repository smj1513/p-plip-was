package com.pplip.domain.user.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class EmailSender {

    private final JavaMailSender sender;

    public void sendMail(String to, String code) throws MessagingException {
        MimeMessage mimeMessage = sender.createMimeMessage();

        // 수신자, 제목, 본문 등 설정
        String subject = "[P plip] 이메일 인증번호";
        String body = "이메일 인증번호입니다. <br/> <h2>%s</h2>".formatted(code);

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body, true);

        // Email 전송
        sender.send(mimeMessage);
    }
}

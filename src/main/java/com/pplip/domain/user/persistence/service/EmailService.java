package com.pplip.domain.user.persistence.service;

import com.pplip.domain.user.api.request.UserRequest;
import jakarta.mail.MessagingException;

public interface EmailService {

    void sendEmail(UserRequest.Email email) throws MessagingException;

    void validate(UserRequest.EmailCheck emailCheck);
}

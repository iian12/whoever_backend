package com.jygoh.whoever.domain.user.email;

public interface EmailService {

    void sendEmail(String to, String subject, String text);

}

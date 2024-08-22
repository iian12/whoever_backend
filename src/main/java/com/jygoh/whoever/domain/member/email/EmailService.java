package com.jygoh.whoever.domain.member.email;

public interface EmailService {

    void sendEmail(String to, String subject, String text);

}

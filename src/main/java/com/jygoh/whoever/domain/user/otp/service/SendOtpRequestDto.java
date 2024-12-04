package com.jygoh.whoever.domain.user.otp.service;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendOtpRequestDto {

    private String username;
    private String email;
}

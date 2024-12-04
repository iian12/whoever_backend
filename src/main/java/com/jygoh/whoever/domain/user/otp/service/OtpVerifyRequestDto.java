package com.jygoh.whoever.domain.user.otp.service;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OtpVerifyRequestDto {

    private String email;
    private String otp;
}

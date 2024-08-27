package com.jygoh.whoever.domain.member.otp.service;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordResetRequestDto {

    private String email;
    private String newPassword;
}

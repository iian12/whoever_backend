package com.jygoh.whoever.domain.user.otp.service;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordResetRequestDto {

    private String email;
    private String newPassword;
}

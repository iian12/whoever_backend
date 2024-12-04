package com.jygoh.whoever.domain.user.otp.service;

public interface PasswordResetService {

    void sendOtp(SendOtpRequestDto requestDto);

    boolean verifyOtp(OtpVerifyRequestDto requestDto);

    void resetPassword(PasswordResetRequestDto requestDto);
}

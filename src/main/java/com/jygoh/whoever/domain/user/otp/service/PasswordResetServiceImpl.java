package com.jygoh.whoever.domain.user.otp.service;

import com.jygoh.whoever.domain.user.email.EmailService;
import com.jygoh.whoever.domain.user.entity.Users;
import com.jygoh.whoever.domain.user.otp.OTPRepository;
import com.jygoh.whoever.domain.user.otp.model.OTP;
import com.jygoh.whoever.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final OTPRepository otpRepository;
    private final BCryptPasswordEncoder bcryptPasswordEncoder;

    public PasswordResetServiceImpl(UserRepository userRepository, EmailService emailService,
        OTPRepository otpRepository, BCryptPasswordEncoder bcryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.otpRepository = otpRepository;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    }

    @Override
    public void sendOtp(SendOtpRequestDto requestDto) {
        String email = requestDto.getEmail();
        Users users = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Member not found."));
        String otpCode = generateOtpCode();
        OTP otpEntity = OTP.builder().users(users).otp(otpCode)
            .expiryTime(LocalDateTime.now().plusMinutes(10)).build();
        otpRepository.save(otpEntity);
        emailService.sendEmail(email, "Password Reset OTP", "Your OTP is: " + otpCode);
    }

    @Override
    public boolean verifyOtp(OtpVerifyRequestDto requestDto) {
        String email = requestDto.getEmail();
        String otp = requestDto.getOtp();
        Users users = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Member not found."));
        OTP otpEntity = otpRepository.findByMemberAndOtp(users, otp)
            .orElseThrow(() -> new IllegalArgumentException("Invalid OTP"));
        if (otpEntity.isExpired()) {
            throw new IllegalArgumentException("OTP has expired");
        }
        return true;
    }

    @Override
    public void resetPassword(PasswordResetRequestDto requestDto) {
        Users users = userRepository.findByEmail(requestDto.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Member not found."));
        users.updatePassword(bcryptPasswordEncoder.encode(requestDto.getNewPassword()));
        userRepository.save(users);
    }

    private String generateOtpCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}

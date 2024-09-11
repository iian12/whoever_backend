package com.jygoh.whoever.domain.member.otp.service;

import com.jygoh.whoever.domain.member.email.EmailService;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.otp.OTPRepository;
import com.jygoh.whoever.domain.member.otp.model.OTP;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PasswordResetServiceImpl implements PasswordResetService {

    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final OTPRepository otpRepository;
    private final BCryptPasswordEncoder bcryptPasswordEncoder;

    public PasswordResetServiceImpl(MemberRepository memberRepository, EmailService emailService,
        OTPRepository otpRepository, BCryptPasswordEncoder bcryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.emailService = emailService;
        this.otpRepository = otpRepository;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    }

    @Override
    public void sendOtp(SendOtpRequestDto requestDto) {
        String email = requestDto.getEmail();
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Member not found."));
        String otpCode = generateOtpCode();
        OTP otpEntity = OTP.builder().member(member).otp(otpCode)
            .expiryTime(LocalDateTime.now().plusMinutes(10)).build();
        otpRepository.save(otpEntity);
        emailService.sendEmail(email, "Password Reset OTP", "Your OTP is: " + otpCode);
    }

    @Override
    public boolean verifyOtp(OtpVerifyRequestDto requestDto) {
        String email = requestDto.getEmail();
        String otp = requestDto.getOtp();
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Member not found."));
        OTP otpEntity = otpRepository.findByMemberAndOtp(member, otp)
            .orElseThrow(() -> new IllegalArgumentException("Invalid OTP"));
        if (otpEntity.isExpired()) {
            throw new IllegalArgumentException("OTP has expired");
        }
        return true;
    }

    @Override
    public void resetPassword(PasswordResetRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Member not found."));
        member.updatePassword(bcryptPasswordEncoder.encode(requestDto.getNewPassword()));
        memberRepository.save(member);
    }

    private String generateOtpCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}

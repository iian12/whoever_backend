package com.jygoh.whoever.domain.member.controller;

import com.jygoh.whoever.domain.member.dto.MemberCreateRequestDto;
import com.jygoh.whoever.domain.member.dto.MemberProfileResponseDto;
import com.jygoh.whoever.domain.member.dto.MyProfileResponseDto;
import com.jygoh.whoever.domain.member.otp.service.OtpVerifyRequestDto;
import com.jygoh.whoever.domain.member.otp.service.PasswordResetRequestDto;
import com.jygoh.whoever.domain.member.otp.service.PasswordResetService;
import com.jygoh.whoever.domain.member.otp.service.SendOtpRequestDto;
import com.jygoh.whoever.domain.member.service.MemberService;
import com.jygoh.whoever.global.security.jwt.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;
    private final PasswordResetService passwordResetService;

    public MemberController(MemberService memberService,
        PasswordResetService passwordResetService) {
        this.memberService = memberService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody MemberCreateRequestDto requestDto) {
        Long memberId = memberService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }

    @GetMapping("/profile/{nickname}")
    public ResponseEntity<MemberProfileResponseDto> getMemberProfile(
        @PathVariable String nickname) {
        MemberProfileResponseDto profile = memberService.getMemberProfileByNickname(nickname);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/me")
    public ResponseEntity<MyProfileResponseDto> getMyProfile(HttpServletRequest request) {
        String token = TokenUtils.extractTokenFromRequest(request);
        MyProfileResponseDto responseDto = memberService.getMyProfile(token);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody SendOtpRequestDto requestDto) {
        try {
            passwordResetService.sendOtp(requestDto);
            return ResponseEntity.ok("OTP sent");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerifyRequestDto requestDto) {
        try {
            boolean isValid = passwordResetService.verifyOtp(requestDto);
            return isValid ? ResponseEntity.ok("OTP is valid.")
                : ResponseEntity.badRequest().body("Invalid OTP.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequestDto requestDto) {
        try {
            passwordResetService.resetPassword(requestDto);
            return ResponseEntity.ok("Password has been reset.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

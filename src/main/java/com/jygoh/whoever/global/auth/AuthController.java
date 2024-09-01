package com.jygoh.whoever.global.auth;

import com.jygoh.whoever.domain.member.dto.MemberLoginRequestDto;
import com.jygoh.whoever.domain.member.otp.service.OtpVerifyRequestDto;
import com.jygoh.whoever.domain.member.otp.service.PasswordResetRequestDto;
import com.jygoh.whoever.domain.member.otp.service.PasswordResetService;
import com.jygoh.whoever.domain.member.otp.service.SendOtpRequestDto;
import com.jygoh.whoever.global.security.jwt.TokenResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService authService, PasswordResetService passwordResetService) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody MemberLoginRequestDto requestDto) {
        TokenResponseDto tokenResponseDto = authService.login(requestDto);
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken",
                tokenResponseDto.getAccessToken()).secure(true).path("/").maxAge(7 * 24 * 60 * 60)
            .sameSite("None").build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken",
                tokenResponseDto.getRefreshToken()).secure(true).httpOnly(true).path("/")
            .maxAge(30 * 24 * 60 * 60).sameSite("None").build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        headers.add("Authorization", "Bearer " + tokenResponseDto.getAccessToken());
        return ResponseEntity.ok().headers(headers).body(tokenResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshToken(@RequestBody String refreshToken) {
        TokenResponseDto tokenResponseDto = authService.refreshToken(refreshToken);
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken",
                tokenResponseDto.getAccessToken()).secure(true).path("/").maxAge(7 * 24 * 60 * 60)
            .sameSite("None").build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        return ResponseEntity.ok().headers(headers).body(null);
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

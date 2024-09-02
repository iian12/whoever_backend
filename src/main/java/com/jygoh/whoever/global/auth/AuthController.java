package com.jygoh.whoever.global.auth;

import com.jygoh.whoever.domain.member.dto.MemberLoginRequestDto;
import com.jygoh.whoever.domain.member.otp.service.OtpVerifyRequestDto;
import com.jygoh.whoever.domain.member.otp.service.PasswordResetRequestDto;
import com.jygoh.whoever.domain.member.otp.service.PasswordResetService;
import com.jygoh.whoever.domain.member.otp.service.SendOtpRequestDto;
import com.jygoh.whoever.domain.member.service.MemberService;
import com.jygoh.whoever.global.auth.dto.CompleteSignupRequest;
import com.jygoh.whoever.global.auth.dto.GoogleUserInfo;
import com.jygoh.whoever.global.security.jwt.TokenResponseDto;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;
    private final MemberService memberService;
    private final OAuth2Service oAuth2Service;

    public AuthController(AuthService authService, PasswordResetService passwordResetService,
        MemberService memberService, OAuth2Service oAuth2Service) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
        this.memberService = memberService;
        this.oAuth2Service = oAuth2Service;
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

    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<?> oauth2GoogleLogin(@RequestParam String code) {
        GoogleUserInfo userInfo = authService.getUserInfoFromGoogle(code);
        TokenResponseDto tokenResponseDto = authService.handleExistingMember(userInfo);
        if (tokenResponseDto == null) {
            // 신규 사용자일 경우 리디렉션 URL 생성
            String redirectUrl = "/complete-signup?userId=" + userInfo.getSub();
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl))
                .build();
        } else {
            // 액세스 토큰과 리프레시 토큰을 쿠키로 설정
            ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken",
                    tokenResponseDto.getAccessToken()).secure(true).httpOnly(true).path("/")
                .maxAge(7 * 24 * 60 * 60) // 7일
                .sameSite("None").build();
            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken",
                    tokenResponseDto.getRefreshToken()).secure(true).httpOnly(true).path("/")
                .maxAge(30 * 24 * 60 * 60) // 30일
                .sameSite("None").build();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
            headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponseDto.getAccessToken());
            return ResponseEntity.ok().headers(headers).build();
        }
    }

    @PostMapping("/complete-signup")
    public ResponseEntity<TokenResponseDto> completeSignup(
        @RequestBody CompleteSignupRequest request) {
        TokenResponseDto tokenResponseDto = authService.completeSignup(request);
        // 액세스 토큰과 리프레시 토큰을 쿠키로 설정
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken",
                tokenResponseDto.getAccessToken()).secure(true).httpOnly(true).path("/")
            .maxAge(7 * 24 * 60 * 60) // 7일
            .sameSite("None").build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken",
                tokenResponseDto.getRefreshToken()).secure(true).httpOnly(true).path("/")
            .maxAge(30 * 24 * 60 * 60) // 30일
            .sameSite("None").build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponseDto.getAccessToken());
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

package com.jygoh.whoever.global.auth;

import com.jygoh.whoever.domain.member.dto.MemberLoginRequestDto;
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

    public AuthController(AuthService authService) {
        this.authService = authService;
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
        // 리프레시 토큰 검증 및 새로운 액세스 토큰 생성
        TokenResponseDto tokenResponseDto = authService.refreshToken(refreshToken);
        // 새로운 액세스 토큰을 쿠키에 설정
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken",
                tokenResponseDto.getAccessToken()).secure(true).path("/")
            .maxAge(7 * 24 * 60 * 60) // 7일 동안 유효
            .sameSite("None").build();
        // 응답 헤더에 쿠키를 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        // 응답 본문은 비워서 반환 (액세스 토큰만 쿠키에 설정)
        return ResponseEntity.ok().headers(headers).body(null);
    }
}

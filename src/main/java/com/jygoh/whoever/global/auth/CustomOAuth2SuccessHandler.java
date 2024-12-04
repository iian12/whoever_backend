package com.jygoh.whoever.global.auth;

import com.jygoh.whoever.domain.user.entity.Users;
import com.jygoh.whoever.domain.user.repository.UserRepository;
import com.jygoh.whoever.global.EncodeDecode;
import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final EncodeDecode encodeDecode;

    public CustomOAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider,
        UserRepository userRepository, EncodeDecode encodeDecode) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.encodeDecode = encodeDecode;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        Long userId = userDetail.getUserId();
        Users users = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not Found"));

        if (!users.isSignUp()) {
            String encodedUserId = encodeDecode.encode(userId);
            response.setHeader("X-USER-ID", encodedUserId);
            response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
        } else {
            String accessToken = jwtTokenProvider.createAccessToken(userDetail.getUserId());
            String refreshToken = jwtTokenProvider.createRefreshToken(userDetail.getUserId());
            Cookie accessTokenCookie = createCookie("accessToken", accessToken);
            Cookie refreshTokenCookie = createCookie("refreshToken", refreshToken);
            response.setStatus(HttpStatus.OK.value());
            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);
        }
    }

    private Cookie createCookie(String name, String token) {
        if (name.equals("accessToken")) {
            Cookie cookie = new Cookie(name, token);
            cookie.setHttpOnly(false);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 2);
            return cookie;
        } else {
            Cookie cookie = new Cookie(name, token);
            cookie.setHttpOnly(false);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 14);
            return cookie;
        }
    }
}

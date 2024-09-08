package com.jygoh.whoever.global.auth;

import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    public CustomOAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        String accessToken = jwtTokenProvider.createAccessToken(userDetail.getMemberId());
        String refreshToken = jwtTokenProvider.createRefreshToken(userDetail.getMemberId());

        Cookie accessTokenCookie = createCookie("accessToken", accessToken);
        Cookie refreshTokenCookie = createCookie("refreshToken", refreshToken);

        logger.info(accessTokenCookie.getValue() + " " + refreshTokenCookie.getValue());

        // 쿠키를 응답에 추가
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        // 클라이언트를 리다이렉트
        response.sendRedirect("http://localhost:3000/login/callback");
    }

    private Cookie createCookie(String name, String token) {
        Cookie cookie = new Cookie(name, token);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(604800);
        return cookie;
    }
}

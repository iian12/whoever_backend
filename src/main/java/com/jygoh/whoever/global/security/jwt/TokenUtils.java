package com.jygoh.whoever.global.security.jwt;

import jakarta.servlet.http.HttpServletRequest;

public class TokenUtils {

    private TokenUtils() {

    }

    public static String extractTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        // "Bearer " 접두사가 있는 경우 제거
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        // 토큰이 없는 경우 null 반환
        return null;
    }

}

package com.jygoh.whoever.global.security.jwt;

import jakarta.servlet.http.HttpServletRequest;

public class TokenUtils {

    private TokenUtils() {

    }

    public static String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new IllegalArgumentException("Invalid or missing Authorization header");
    }

}

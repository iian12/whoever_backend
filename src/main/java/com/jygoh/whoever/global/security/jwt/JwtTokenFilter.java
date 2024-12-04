package com.jygoh.whoever.global.security.jwt;

import com.jygoh.whoever.global.auth.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider,
        CustomUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/rss/") || path.startsWith("/posts/category") || path.startsWith("/images")) {
            filterChain.doFilter(request, response);  // 필터 통과 (바로 다음 필터로 이동)
            return;
        }
        // Request에서 JWT 토큰을 추출합니다.
        String token = resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하다면, 사용자 ID를 추출합니다.
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            // 사용자 ID를 사용하여 UserDetails를 로드합니다.
            UserDetails userDetails = userDetailsService.loadUserById(userId);
            // 인증 객체를 생성합니다.
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
            // WebAuthenticationDetailsSource를 사용하여 인증 정보를 설정합니다.
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Spring Security의 SecurityContext에 인증 정보를 설정합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 다음 필터 체인으로 요청을 전달합니다.
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

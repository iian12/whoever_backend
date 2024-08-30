package com.jygoh.whoever.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-validity-in-ms}")
    private long accessTokenValidityInMilliseconds;// 1시간

    @Value("${jwt.refresh-token-validity-in-ms}")
    private long refreshTokenValidityInMilliseconds; // 7일

    private Key key;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getUrlDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Long memberId) {
        if (memberId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", memberId);
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);
        return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public String createRefreshToken(Long memberId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityInMilliseconds);
        return Jwts.builder().setSubject(memberId.toString()).setIssuedAt(now)
            .setExpiration(validity).signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public boolean validateToken(String token) {
        try {
            if (token == null || token.isEmpty()) {
                log.error("Token is null or empty");
                return false;
            }
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public Long getMemberIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                .getBody();
            // Long 타입으로 클레임 값을 가져옵니다
            Number memberIdNumber = claims.get("memberId", Number.class);
            if (memberIdNumber == null) {
                throw new IllegalArgumentException("User ID in token cannot be null or empty");
            }
            return memberIdNumber.longValue();
        } catch (JwtException e) {
            // JWT 예외 처리
            throw new IllegalArgumentException("Invalid JWT token", e);
        } catch (Exception e) {
            // 기타 예외 처리
            throw new RuntimeException("Unexpected error occurred", e);
        }
    }
}

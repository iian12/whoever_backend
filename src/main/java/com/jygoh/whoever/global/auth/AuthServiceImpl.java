package com.jygoh.whoever.global.auth;

import com.jygoh.whoever.domain.member.dto.MemberLoginRequestDto;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import com.jygoh.whoever.global.security.jwt.RefreshToken;
import com.jygoh.whoever.global.security.jwt.RefreshTokenRepository;
import com.jygoh.whoever.global.security.jwt.TokenResponseDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthServiceImpl(JwtTokenProvider jwtTokenProvider,
                           RefreshTokenRepository refreshTokenRepository,
                           MemberRepository memberRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public TokenResponseDto login(MemberLoginRequestDto requestDto) {
        // 사용자 인증 로직
        Member member = memberRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        Long userId = member.getId();

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);

        // 기존의 refreshToken이 있다면 삭제하고 새로운 refreshToken 저장
        refreshTokenRepository.deleteByUserId(userId);
        refreshTokenRepository.save(RefreshToken.builder()
                .userId(userId)
                .token(refreshToken)
                .build());

        // TokenResponseDto 객체 생성
        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenResponseDto refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        // 기존 refreshToken 검증
        RefreshToken existingRefreshToken = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token does not exist or is invalid"));
        if (!existingRefreshToken.getToken().equals(refreshToken)) {
            throw new IllegalArgumentException("Refresh token does not match");
        }

        // 새로운 accessToken과 refreshToken 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(userId);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userId);

        // 새로운 refreshToken 저장
        RefreshToken updatedRefreshToken = existingRefreshToken.updateToken(newRefreshToken);
        refreshTokenRepository.save(updatedRefreshToken);

        // TokenResponseDto 객체 생성
        return TokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}

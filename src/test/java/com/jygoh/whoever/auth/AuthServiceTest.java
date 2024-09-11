package com.jygoh.whoever.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jygoh.whoever.domain.member.dto.MemberCreateRequestDto;
import com.jygoh.whoever.domain.member.dto.MemberLoginRequestDto;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import com.jygoh.whoever.domain.member.service.MemberService;
import com.jygoh.whoever.global.auth.AuthService;
import com.jygoh.whoever.global.security.jwt.RefreshToken;
import com.jygoh.whoever.global.security.jwt.RefreshTokenRepository;
import com.jygoh.whoever.global.security.jwt.TokenResponseDto;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        refreshTokenRepository.deleteAll();
    }

    @Test
    public void testRegisterAndLogin() {
        MemberCreateRequestDto createRequest = MemberCreateRequestDto.builder().username("testuser")
            .password("password").email("email@example.com").nickname("nickname").build();
        Long memberId = memberService.registerMember(createRequest);
        assertNotNull(memberId);
        MemberLoginRequestDto loginRequest = MemberLoginRequestDto.builder().username("testuser")
            .password("password").build();
        TokenResponseDto tokenResponse = authService.login(loginRequest);
        // 3. 로그인 성공 시 토큰 발급 확인
        assertNotNull(tokenResponse.getAccessToken());
        assertNotNull(tokenResponse.getRefreshToken());
        // 4. DB에 저장된 토큰 검증
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberId(memberId);
        assertTrue(refreshToken.isPresent());
        assertEquals(tokenResponse.getRefreshToken(), refreshToken.get().getToken());
    }
}

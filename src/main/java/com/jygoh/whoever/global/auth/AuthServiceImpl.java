package com.jygoh.whoever.global.auth;

import com.jygoh.whoever.domain.member.dto.MemberLoginRequestDto;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.entity.Provider;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import com.jygoh.whoever.global.auth.dto.CompleteSignupRequest;
import com.jygoh.whoever.global.auth.dto.GoogleTokenResponse;
import com.jygoh.whoever.global.auth.dto.GoogleUserInfo;
import com.jygoh.whoever.global.config.HttpSessionService;
import com.jygoh.whoever.global.exception.RedirectException;
import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import com.jygoh.whoever.global.security.jwt.RefreshToken;
import com.jygoh.whoever.global.security.jwt.RefreshTokenRepository;
import com.jygoh.whoever.global.security.jwt.TokenResponseDto;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final WebClient webClient;
    private final HttpSessionService httpSessionService;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.google.token-uri}")
    private String tokenUri;
    @Value("${spring.security.oauth2.client.registration.google.user-info-uri}")
    private String userInfoUri;

    public AuthServiceImpl(JwtTokenProvider jwtTokenProvider,
        RefreshTokenRepository refreshTokenRepository, MemberRepository memberRepository,
        WebClient.Builder webClientBuilder, BCryptPasswordEncoder passwordEncoder,
        HttpSessionService httpSessionService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.webClient = webClientBuilder.baseUrl(tokenUri).build();
        this.httpSessionService = httpSessionService;
    }

    @Override
    public TokenResponseDto login(MemberLoginRequestDto requestDto) {
        Member member = memberRepository.findByUsername(requestDto.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        String accessToken = jwtTokenProvider.createAccessToken(member.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
        refreshTokenRepository.deleteByMemberId(member.getId());
        refreshTokenRepository.save(new RefreshToken(member.getId(), refreshToken));
        return new TokenResponseDto(accessToken, refreshToken);
    }

    private String getAccessToken(String code) {
        return WebClient.create("https://oauth2.googleapis.com").post().uri("/token").bodyValue(
                "code=" + code + "&client_id=" + clientId + "&client_secret=" + clientSecret
                    + "&redirect_uri=" + redirectUri + "&grant_type=authorization_code").retrieve()
            .bodyToMono(GoogleTokenResponse.class).map(GoogleTokenResponse::getAccessToken).block();
    }

    @Override
    public GoogleUserInfo getUserInfoFromGoogle(String code) {
        String accessToken = getAccessToken(code);
        return WebClient.create("https://oauth2.googleapis.com").get()
            .uri("/userinfo?access_token={accessToken}", accessToken).retrieve()
            .bodyToMono(GoogleUserInfo.class).block();
    }

    @Override
    public TokenResponseDto handleExistingMember(GoogleUserInfo userInfo) {
        // 이메일로 기존 사용자 찾기
        Member member = memberRepository.findByEmail(userInfo.getEmail()).orElseThrow(
            () -> new IllegalArgumentException(
                "No member found with email: " + userInfo.getEmail()));
        // 회원 정보 업데이트
        member.addProvider(Provider.GOOGLE, userInfo.getSub());
        member.updateProfileImageUrl(userInfo.getPicture());
        memberRepository.save(member);
        // 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(member.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
        // 기존 리프레시 토큰 삭제 및 새 리프레시 토큰 저장
        refreshTokenRepository.deleteByMemberId(member.getId());
        refreshTokenRepository.save(new RefreshToken(member.getId(), refreshToken));
        // TokenResponseDto 반환
        return new TokenResponseDto(accessToken, refreshToken);
    }

    @Override
    public TokenResponseDto handleNewMember(GoogleUserInfo userInfo) {
        Member newMember = Member.builder().email(userInfo.getEmail()).providerId(userInfo.getSub())
            .providers(Set.of(Provider.GOOGLE)).profileImageUrl(userInfo.getPicture())
            .signupCompleted(false) // 회원가입 미완료 상태로 설정
            .build();
        Member savedMember = memberRepository.save(newMember);
        // 이메일을 세션에 저장
        httpSessionService.setAttribute("email", userInfo.getEmail());
        // 추가 정보를 입력하기 위한 URL을 반환
        String redirectUrl = "/complete-signup?userId=" + savedMember.getId();
        // Redirect URL을 사용하여 클라이언트를 리디렉션하거나 추가 정보를 입력할 수 있는 페이지로 안내
        throw new RedirectException(redirectUrl);
    }

    @Override
    public Mono<GoogleUserInfo> getUserInfo(String accessToken) {
        return webClient.get().uri(userInfoUri)
            .headers(headers -> headers.setBearerAuth(accessToken)).retrieve()
            .bodyToMono(GoogleUserInfo.class);
    }

    @Override
    public TokenResponseDto completeSignup(CompleteSignupRequest request) {
        // 사용자를 ID로 찾기
        Member member = memberRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        // 사용자 정보를 업데이트
        member.completeSignup(request.getNickname(), encodedPassword);
        // 변경된 사용자 정보를 저장
        Member savedMember = memberRepository.save(member);
        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(savedMember.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(savedMember.getId());
        // RefreshToken 저장
        refreshTokenRepository.deleteByMemberId(savedMember.getId()); // 이전 토큰 삭제
        refreshTokenRepository.save(new RefreshToken(savedMember.getId(), refreshToken)); // 새 토큰 저장
        // 토큰 응답 반환
        return new TokenResponseDto(accessToken, refreshToken);
    }

    @Override
    public TokenResponseDto refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        Long memberId = jwtTokenProvider.getMemberIdFromToken(refreshToken);
        RefreshToken existingRefreshToken = refreshTokenRepository.findByMemberId(memberId)
            .orElseThrow(
                () -> new IllegalArgumentException("Refresh token does not exist or is invalid"));
        if (!existingRefreshToken.getToken().equals(refreshToken)) {
            throw new IllegalArgumentException("Refresh token does not match");
        }
        String newAccessToken = jwtTokenProvider.createAccessToken(memberId);
        // TokenResponseDto 객체 생성
        return TokenResponseDto.builder().accessToken(newAccessToken).build();
    }
}

package com.jygoh.whoever.global.auth;

import com.jygoh.whoever.domain.member.dto.MemberLoginRequestDto;
import com.jygoh.whoever.global.auth.dto.CompleteSignupRequest;
import com.jygoh.whoever.global.auth.dto.GoogleUserInfo;
import com.jygoh.whoever.global.security.jwt.TokenResponseDto;
import reactor.core.publisher.Mono;

public interface AuthService {

    TokenResponseDto login(MemberLoginRequestDto requestDto);

    GoogleUserInfo getUserInfoFromGoogle(String code);

    TokenResponseDto handleExistingMember(GoogleUserInfo userInfo);

    TokenResponseDto handleNewMember(GoogleUserInfo userInfo);

    Mono<GoogleUserInfo> getUserInfo(String accessToken);

    TokenResponseDto completeSignup(CompleteSignupRequest request);

    TokenResponseDto refreshToken(String refreshToken);

}

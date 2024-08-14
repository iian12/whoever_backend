package com.jygoh.whoever.global.auth;

import com.jygoh.whoever.domain.member.dto.MemberLoginRequestDto;
import com.jygoh.whoever.global.security.jwt.TokenResponseDto;

public interface AuthService {

    TokenResponseDto login(MemberLoginRequestDto requestDto);

    TokenResponseDto refreshToken(String refreshToken);
}

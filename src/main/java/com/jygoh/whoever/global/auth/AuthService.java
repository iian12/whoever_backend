package com.jygoh.whoever.global.auth;

import com.jygoh.whoever.domain.user.dto.UserLoginReqDto;
import com.jygoh.whoever.global.security.jwt.TokenResponseDto;

public interface AuthService {

    TokenResponseDto login(UserLoginReqDto requestDto);

    TokenResponseDto refreshToken(String refreshToken);
}

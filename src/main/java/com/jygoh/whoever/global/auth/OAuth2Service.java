package com.jygoh.whoever.global.auth;

import com.jygoh.whoever.global.auth.dto.GoogleTokenValidation;
import com.jygoh.whoever.global.auth.dto.GoogleUserInfo;

public interface OAuth2Service {

    GoogleUserInfo getUserInfo(String accessToken);

    GoogleTokenValidation validateToken(String accessToken);
}

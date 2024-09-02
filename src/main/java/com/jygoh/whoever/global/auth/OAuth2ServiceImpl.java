package com.jygoh.whoever.global.auth;

import com.jygoh.whoever.global.auth.dto.GoogleTokenValidation;
import com.jygoh.whoever.global.auth.dto.GoogleUserInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class OAuth2ServiceImpl implements OAuth2Service {

    private final RestTemplate restTemplate;
    private final String googleUserInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
    private final String googleTokenValidationUrl = "https://www.googleapis.com/oauth2/v3/tokeninfo";

    public OAuth2ServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public GoogleUserInfo getUserInfo(String accessToken) {
        // 액세스 토큰을 사용하여 Google API에서 사용자 정보 가져오기
        String url = googleUserInfoUrl + "?access_token=" + accessToken;
        ResponseEntity<GoogleUserInfo> response = restTemplate.getForEntity(url,
            GoogleUserInfo.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch user info from Google");
        }
    }

    // 액세스 토큰의 유효성을 확인하는 메서드
    public GoogleTokenValidation validateToken(String accessToken) {
        String url = googleTokenValidationUrl + "?id_token=" + accessToken;
        ResponseEntity<GoogleTokenValidation> response = restTemplate.getForEntity(url,
            GoogleTokenValidation.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Invalid or expired token");
        }
    }
}

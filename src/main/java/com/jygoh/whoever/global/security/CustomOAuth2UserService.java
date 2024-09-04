package com.jygoh.whoever.global.security;

import com.jygoh.whoever.domain.member.entity.Provider;
import com.jygoh.whoever.domain.member.service.MemberService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    public CustomOAuth2UserService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 사용자 정보 추출
        String providerId = (String) oAuth2User.getAttributes().get("sub");
        String email = (String) oAuth2User.getAttributes().get("email");
        String name = (String) oAuth2User.getAttributes().get("name");
        String profileImageUrl = (String) oAuth2User.getAttributes().get("picture");

        // 제공자 추출
        Provider provider = mapToProvider(userRequest.getClientRegistration().getRegistrationId());

        // MemberService의 processOAuth2User 메서드 호출
        Long memberId = memberService.processOAuth2User(email, name, provider, profileImageUrl, providerId);

        // CustomOAuth2User 생성 및 반환
        return new CustomOAuth2User(memberId, oAuth2User.getAttributes(), "sub");
    }

    private Provider mapToProvider(String registrationId) {
        return switch (registrationId) {
            case "google" -> Provider.GOOGLE;
            default -> throw new IllegalArgumentException("Unknown provider: " + registrationId);
        };
    }
}

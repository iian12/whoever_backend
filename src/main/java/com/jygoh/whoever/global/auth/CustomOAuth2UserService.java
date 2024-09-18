package com.jygoh.whoever.global.auth;

import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.entity.Provider;
import com.jygoh.whoever.domain.member.entity.Role;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import java.util.Collections;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        Provider oauthProvider = getProviderFromRegistrationId(provider);
        String email = getAttribute(oAuth2User, provider, "email");
        String nickname = getAttribute(oAuth2User, provider, "name");
        String profileImageUrl = getAttribute(oAuth2User, provider, "picture");
        String providerId = getAttribute(oAuth2User, provider, "sub");
        Member member = memberRepository.findByEmail(email).map(existingMember -> {
            existingMember.addProvider(oauthProvider, providerId);
            return memberRepository.save(existingMember);
        }).orElseGet(() -> {
            Member newMember = Member.builder().email(email).nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .providers(Collections.singletonList(oauthProvider)).providerId(providerId)
                .role(Role.MEMBER).build();
            return memberRepository.save(newMember);
        });
        return new CustomUserDetail(oAuth2User, member.getId());
    }

    private String getAttribute(OAuth2User oAuth2User, String provider, String attributeName) {
        return switch (provider) {
            case "google" -> (String) oAuth2User.getAttribute(attributeName);
            // 다른 제공자에 대한 추가 처리
            default -> throw new IllegalArgumentException("Unknown provider: " + provider);
        };
    }

    private Provider getProviderFromRegistrationId(String registrationId) {
        if (registrationId.equals("google")) {
            return Provider.GOOGLE;
        }
        throw new IllegalArgumentException("Unknown provider: " + registrationId);
    }

}

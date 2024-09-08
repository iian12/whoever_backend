package com.jygoh.whoever.global.auth;

import com.jygoh.whoever.domain.member.entity.Member;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomUserDetail implements UserDetails, OAuth2User {

    private final Member member;
    private final OAuth2User oAuth2User;
    private final Long userId;

    // 생성자
    public CustomUserDetail(Member member, Long userId) {
        this.member = member;
        this.oAuth2User = null;
        this.userId = userId;
    }

    public CustomUserDetail(OAuth2User oAuth2User, Long userId) {
        this.member = null;
        this.oAuth2User = oAuth2User;
        this.userId = userId;
    }

    // UserDetails 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (member != null) {
            // 반환할 권한 목록 구현
            return null; // 실제 권한을 반환하도록 수정
        } else if (oAuth2User != null) {
            return oAuth2User.getAuthorities();
        }
        return null;
    }

    @Override
    public String getPassword() {
        return member != null ? member.getPassword() : null;
    }

    @Override
    public String getUsername() {
        return member != null ? member.getEmail() : oAuth2User.getAttribute("email");
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // OAuth2User 메서드
    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User != null ? oAuth2User.getAttributes() : null;
    }

    @Override
    public String getName() {
        return oAuth2User != null ? oAuth2User.getName() : null;
    }

    public Long getMemberId() {
        return userId;
    }

    public Member getMember() {
        return member;
    }
}

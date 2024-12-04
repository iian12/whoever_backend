package com.jygoh.whoever.global.auth;

import com.jygoh.whoever.domain.user.entity.Users;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomUserDetail implements UserDetails, OAuth2User {

    private final Users users;
    private final OAuth2User oAuth2User;
    private final Long userId;

    // 생성자
    public CustomUserDetail(Users users, Long userId) {
        this.users = users;
        this.oAuth2User = null;
        this.userId = userId;
    }

    public CustomUserDetail(OAuth2User oAuth2User, Long userId) {
        this.users = null;
        this.oAuth2User = oAuth2User;
        this.userId = userId;
    }

    // UserDetails 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (users != null) {
            // 반환할 권한 목록 구현
            return null; // 실제 권한을 반환하도록 수정
        } else if (oAuth2User != null) {
            return oAuth2User.getAuthorities();
        }
        return null;
    }

    @Override
    public String getPassword() {
        return users != null ? users.getPassword() : null;
    }

    @Override
    public String getUsername() {
        return users != null ? users.getEmail() : Objects.requireNonNull(oAuth2User).getAttribute("email");
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

    public Long getUserId() {
        return userId;
    }

    public Users getMember() {
        return users;
    }

    public boolean isSignUp() {
        return users != null && users.isSignUp();
    }
}

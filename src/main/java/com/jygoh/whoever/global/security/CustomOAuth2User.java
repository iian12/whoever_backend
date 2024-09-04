package com.jygoh.whoever.global.security;

import java.util.Collections;
import java.util.Map;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User extends DefaultOAuth2User implements OAuth2User {

    private final Long id;

    public CustomOAuth2User(Long id, Map<String, Object> attributes, String nameAttributeKey) {
        super(Collections.emptyList(), attributes, nameAttributeKey);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}

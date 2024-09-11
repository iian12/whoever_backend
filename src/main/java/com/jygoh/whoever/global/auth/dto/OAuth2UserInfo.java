package com.jygoh.whoever.global.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuth2UserInfo {

    private String id;
    private String email;
    private String name;
    private String picture;

    public OAuth2UserInfo(String id, String email, String name, String picture) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
    }
}

package com.jygoh.whoever.global.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoResponse {

    private String email;
    private String profileImageUrl;

    @Builder
    public UserInfoResponse(String email, String profileImageUrl) {
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }
}

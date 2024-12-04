package com.jygoh.whoever.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateReqDto {

    private String email;

    private String password;

    private String nickname;

    private String profileImageUrl;

    @Builder
    public UserUpdateReqDto(String email, String password, String nickname,
        String profileImageUrl) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}

package com.jygoh.whoever.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateRequestDto {

    private String username;

    private String password;

    private String email;

    private String nickname;

    private String profileImageUrl;

    @Builder
    public MemberUpdateRequestDto(String username, String password, String email, String nickname,
        String profileImageUrl) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}

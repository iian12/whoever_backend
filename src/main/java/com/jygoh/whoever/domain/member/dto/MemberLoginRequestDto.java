package com.jygoh.whoever.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLoginRequestDto {

    private String username;
    private String password;

    @Builder
    public MemberLoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

package com.jygoh.whoever.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreateRequestDto {

    private String username;

    private String password;

    private String email;

    @Builder
    public MemberCreateRequestDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}

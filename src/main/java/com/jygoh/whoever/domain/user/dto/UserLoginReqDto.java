package com.jygoh.whoever.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginReqDto {

    private String email;
    private String password;

    @Builder
    public UserLoginReqDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

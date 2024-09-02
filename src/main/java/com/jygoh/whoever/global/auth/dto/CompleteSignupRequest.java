package com.jygoh.whoever.global.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompleteSignupRequest {

    private String email;
    private String nickname;
    private String password;
    private String profileImageUrl;
}

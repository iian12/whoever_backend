package com.jygoh.whoever.global.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthResponse {

    private String token;

    @Builder
    public AuthResponse(String token) {
        this.token = token;
    }
}

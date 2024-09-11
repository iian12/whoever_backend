package com.jygoh.whoever.global.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleUserInfo {

    private String sub;
    private String name;
    private String email;
    private String picture;
}

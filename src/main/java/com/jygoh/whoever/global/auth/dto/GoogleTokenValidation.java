package com.jygoh.whoever.global.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleTokenValidation {

    private String sub;
    private String email;
    private String name;
    private String picture;

}

package com.jygoh.whoever.domain.member.profile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NicknameRequestDto {

    @NotBlank(message = "Enter Nickname")
    private String nickname;

    @Builder
    public NicknameRequestDto(String nickname) {
        this.nickname = nickname;
    }
}

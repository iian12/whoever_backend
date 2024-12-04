package com.jygoh.whoever.domain.user.dto;

import com.jygoh.whoever.domain.user.entity.Users;
import com.jygoh.whoever.domain.user.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCreateReqDto {

    private String email;
    private String password;
    private String nickname;

    @Builder
    public UserCreateReqDto(String password, String email, String nickname) {
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public Users toEntity() {
        return Users.builder().password(this.password).email(this.email)
            .nickname(this.nickname).role(Role.MEMBER).build();
    }
}

package com.jygoh.whoever.domain.member.dto;

import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberCreateRequestDto {

    private String username;
    private String password;
    private String email;
    private String nickname;

    @Builder
    public MemberCreateRequestDto(String username, String password, String email, String nickname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public Member toEntity() {
        return Member.builder().username(this.username).password(this.password).email(this.email)
            .nickname(this.nickname).role(Role.MEMBER).build();
    }
}

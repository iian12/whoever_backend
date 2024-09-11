package com.jygoh.whoever.domain.member.dto;

import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberCreateRequestDto {

    private String email;
    private String password;
    private String nickname;

    @Builder
    public MemberCreateRequestDto(String password, String email, String nickname) {
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public Member toEntity() {
        return Member.builder().password(this.password).email(this.email)
            .nickname(this.nickname).role(Role.MEMBER).build();
    }
}

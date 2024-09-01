package com.jygoh.whoever.domain.member.dto;

import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.entity.Provider;
import com.jygoh.whoever.domain.member.entity.Role;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SocialLoginRequestDto {

    private String email;
    private Provider provider;
    private String providerId;
    private String nickname;


    public Member toEntity() {
        return Member.builder().email(this.email).providers(Set.of(this.provider))
            .providerId(this.providerId).nickname(this.nickname).role(Role.MEMBER).build();
    }
}

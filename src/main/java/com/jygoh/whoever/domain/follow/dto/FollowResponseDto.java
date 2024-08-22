package com.jygoh.whoever.domain.follow.dto;

import com.jygoh.whoever.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowResponseDto {

    private Long id;
    private String nickname;

    public FollowResponseDto(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
    }
}

package com.jygoh.whoever.domain.follow.dto;

import com.jygoh.whoever.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowResponseDto {

    private Long id;
    private String nickname;

    public FollowResponseDto(Long id, String nickname) {
        // followee 또는 follower의 ID와 닉네임을 사용하여 DTO를 생성합니다.
        this.id = id;
        this.nickname = nickname;
    }

    public FollowResponseDto(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
    }
}

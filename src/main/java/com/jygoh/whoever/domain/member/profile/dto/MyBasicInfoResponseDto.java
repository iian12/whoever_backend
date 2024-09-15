package com.jygoh.whoever.domain.member.profile.dto;

import com.jygoh.whoever.domain.follow.dto.FollowResponseDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyBasicInfoResponseDto {

    private String email;
    private String nickname;
    private String profileImageUrl;
    private List<FollowResponseDto> following;
    private int followerCount;

    @Builder
    public MyBasicInfoResponseDto(String email, String nickname, String profileImageUrl,
        List<FollowResponseDto> following, int followerCount) {
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.following = following;
        this.followerCount = followerCount;
    }
}

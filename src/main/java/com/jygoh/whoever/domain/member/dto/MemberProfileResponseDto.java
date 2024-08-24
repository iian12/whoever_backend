package com.jygoh.whoever.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MemberProfileResponseDto {

    private String nickname;
    private List<PostForProfileDto> posts;
    private int followerCount;

    @Builder
    public MemberProfileResponseDto(String nickname, List<PostForProfileDto> posts, int followerCount) {
        this.nickname = nickname;
        this.posts = posts;
        this.followerCount = followerCount;
    }

    @Getter
    @NoArgsConstructor
    public static class PostForProfileDto {
        private Long id;
        private String title;

        public PostForProfileDto(Long id, String title) {
            this.id = id;
            this.title = title;
        }
    }
}

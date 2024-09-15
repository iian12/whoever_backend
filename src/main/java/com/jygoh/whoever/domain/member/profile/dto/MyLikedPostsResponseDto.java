package com.jygoh.whoever.domain.member.profile.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyLikedPostsResponseDto {

    private List<PostForProfileDto> likedPosts;

    @Builder
    public MyLikedPostsResponseDto(List<PostForProfileDto> likedPosts) {
        this.likedPosts = likedPosts;
    }

    @Getter
    @NoArgsConstructor
    public static class PostForProfileDto {

        private Long id;
        private String title;
        private String content;

        public PostForProfileDto(Long id, String title, String content) {
            this.id = id;
            this.title = title;
            this.content = content;
        }
    }
}

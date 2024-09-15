package com.jygoh.whoever.domain.member.profile.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyPostsResponseDto {

    private List<PostForProfileDto> posts;

    @Builder
    public MyPostsResponseDto(List<PostForProfileDto> posts) {
        this.posts = posts;
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

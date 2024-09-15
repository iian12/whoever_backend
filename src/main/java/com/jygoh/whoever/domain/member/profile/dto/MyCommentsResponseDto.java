package com.jygoh.whoever.domain.member.profile.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyCommentsResponseDto {

    private List<CommentForProfileDto> comments;

    @Builder
    public MyCommentsResponseDto(List<CommentForProfileDto> comments) {
        this.comments = comments;
    }

    @Getter
    @NoArgsConstructor
    public static class CommentForProfileDto {

        private Long id;
        private String content;
        private PostForCommentDto post;

        public CommentForProfileDto(Long id, String content, PostForCommentDto post) {
            this.id = id;
            this.content = content;
            this.post = post;
        }

        @Getter
        @NoArgsConstructor
        public static class PostForCommentDto {

            private Long id;
            private String title;

            public PostForCommentDto(Long id, String title) {
                this.id = id;
                this.title = title;
            }
        }
    }
}

package com.jygoh.whoever.domain.member.dto;

import com.jygoh.whoever.domain.comment.model.Comment;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.post.model.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class MemberProfileResponseDto {

    private String nickname;
    private List<MemberProfileResponseDto.PostForProfileDto> posts;
    private List<MemberProfileResponseDto.CommentForProfileDto> comments;
    private int followerCount;

    @Builder
    public MemberProfileResponseDto(String nickname,
        List<PostForProfileDto> posts, List<CommentForProfileDto> comments,
        int followerCount) {
        this.nickname = nickname;
        this.posts = posts;
        this.comments = comments;
        this.followerCount = followerCount;
    }

    @Getter
    @NoArgsConstructor
    public static class PostForProfileDto {
        private Long id;
        private String title;

        public PostForProfileDto(Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CommentForProfileDto {
        private Long id;
        private String content;
        private MemberProfileResponseDto.CommentForProfileDto.PostForCommentDto post;

        public CommentForProfileDto(Comment comment) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.post = new MemberProfileResponseDto.CommentForProfileDto.PostForCommentDto(comment.getPost());
        }

        @Getter
        @NoArgsConstructor
        public static class PostForCommentDto {
            private Long id;
            private String title;

            public PostForCommentDto(Post post) {
                this.id = post.getId();
                this.title = post.getTitle();
            }
        }
    }
}

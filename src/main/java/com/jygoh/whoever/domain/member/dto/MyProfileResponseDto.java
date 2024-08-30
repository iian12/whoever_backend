package com.jygoh.whoever.domain.member.dto;

import com.jygoh.whoever.domain.follow.dto.FollowResponseDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyProfileResponseDto {

    private String username;
    private String email;
    private String nickname;
    private String role;
    private List<PostForProfileDto> posts;
    private List<CommentForProfileDto> comments;
    private List<FollowResponseDto> following;
    private int followerCount;

    @Builder
    public MyProfileResponseDto(String username, String email, String nickname, String role,
        List<PostForProfileDto> posts, List<CommentForProfileDto> comments,
        List<FollowResponseDto> following, int followerCount) {
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.posts = posts;
        this.comments = comments;
        this.following = following;
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

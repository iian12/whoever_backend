package com.jygoh.whoever.domain.member.dto;

import com.jygoh.whoever.domain.comment.model.Comment;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.post.model.Post;
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
    private List<FollowForProfileDto> following;
    private int followerCount;

    @Builder
    public MyProfileResponseDto(String username, String email, String nickname,
        String role, List<PostForProfileDto> posts,
        List<CommentForProfileDto> comments, List<FollowForProfileDto> following,
        int followerCount) {
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
        private PostForCommentDto post;

        public CommentForProfileDto(Comment comment) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.post = new PostForCommentDto(comment.getPost());
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

    @Getter
    @NoArgsConstructor
    public static class FollowForProfileDto {
        private Long id;
        private String nickname;

        public FollowForProfileDto(Member followee) {
            this.id = followee.getId();
            this.nickname = followee.getNickname();
        }
    }
}

package com.jygoh.whoever.domain.member.dto;

import com.jygoh.whoever.domain.comment.model.Comment;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.post.model.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class MemberProfileResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private List<MemberProfileResponseDto.PostForProfileDto> posts;
    private List<MemberProfileResponseDto.CommentForProfileDto> comments;

    public MemberProfileResponseDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.posts = member.getPosts()
                .stream().map(MemberProfileResponseDto.PostForProfileDto::new).collect(Collectors.toList());
        this.comments = member.getComments()
                .stream().map(MemberProfileResponseDto.CommentForProfileDto::new).collect(Collectors.toList());
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

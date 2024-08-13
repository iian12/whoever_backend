package com.jygoh.whoever.domain.member.dto;

import com.jygoh.whoever.domain.comment.model.Comment;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.post.model.Post;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberProfileResponseDto {

    private Long id;
    private String username;
    private String email;
    private String role;
    private List<PostForProfileDto> posts;
    private List<CommentForProfileDto> comments;

    public MemberProfileResponseDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.role = member.getRole().name();
        this.posts = member.getPosts()
            .stream().map(PostForProfileDto::new).collect(Collectors.toList());
        this.comments = member.getComments()
            .stream().map(CommentForProfileDto::new).collect(Collectors.toList());
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

        public CommentForProfileDto(Comment comment) {
            this.id = comment.getId();
            this.content = comment.getContent();
        }
    }
}

package com.jygoh.whoever.domain.comment.dto;

import com.jygoh.whoever.domain.comment.model.Comment;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.post.model.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentCreateRequestDto {

    private Long postId;
    private Long authorId;
    private String authorNickname;
    private String content;
    private Long parentCommentId;

    @Builder
    public CommentCreateRequestDto(Long postId, Long authorId, String authorNickname, String content, Long parentCommentId) {
        this.postId = postId;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
        this.content = content;
        this.parentCommentId = parentCommentId;
    }

    public Comment toEntity(Post post, Member author, String authorNickname, Comment parentComment) {
        return Comment.builder()
                .post(post)
                .author(author)
                .authorNickname(authorNickname)
                .content(this.content)
                .parentComment(parentComment)
                .createdAt(LocalDateTime.now())
                .isUpdated(false)
                .build();
    }
}

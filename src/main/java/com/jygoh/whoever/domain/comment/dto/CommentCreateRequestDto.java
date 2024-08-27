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

    private Long postId; // 게시글 ID

    private Long authorId; // 작성자 ID

    private String authorNickname; // 작성자 닉네임

    private String content; // 댓글 내용

    private Long parentCommentId; // 부모 댓글 ID

    @Builder
    public CommentCreateRequestDto(Long postId, Long authorId, String authorNickname, String content, Long parentCommentId) {
        this.postId = postId;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
        this.content = content;
        this.parentCommentId = parentCommentId;
    }

    public Comment toEntity(Post post, Member author) {
        return Comment.builder()
                .postId(post.getId())
                .authorId(author.getId())
                .authorNickname(author.getNickname())
                .content(this.content)
                .parentCommentId(this.parentCommentId)
                .createdAt(LocalDateTime.now())
                .isUpdated(false)
                .build();
    }
}

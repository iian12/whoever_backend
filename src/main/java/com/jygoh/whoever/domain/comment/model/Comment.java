package com.jygoh.whoever.domain.comment.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long authorId;

    private String authorNickname;

    @Lob
    private String content;

    @Column
    private Long parentCommentId; // 부모 댓글 ID

    @ElementCollection
    @CollectionTable(name = "CommentReplies", joinColumns = @JoinColumn(name = "comment_id"))
    @Column(name = "reply_id")
    private List<Long> replyIds = new ArrayList<>();

    private LocalDateTime createdAt;

    private Boolean isUpdated;

    @Builder
    public Comment(Long postId, Long authorId, String authorNickname, String content, Long parentCommentId, List<Long> replyIds, LocalDateTime createdAt, Boolean isUpdated) {
        this.postId = postId;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
        this.content = content;
        this.parentCommentId = parentCommentId;
        this.replyIds = replyIds != null ? replyIds : new ArrayList<>();
        this.createdAt = createdAt;
        this.isUpdated = isUpdated;
    }

    public void updateComment(String content) {
        this.content = content;
        this.isUpdated = true;
    }
}

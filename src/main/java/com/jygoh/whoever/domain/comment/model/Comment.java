package com.jygoh.whoever.domain.comment.model;

import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.post.model.Post;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    private String authorNickname;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<Comment> replies = new ArrayList<>();

    private LocalDateTime createdAt;

    private Boolean isUpdated;

    @Builder
    public Comment(Post post, Member author, String authorNickname, String content, Comment parentComment, List<Comment> replies, LocalDateTime createdAt, Boolean isUpdated) {
        this.post = post;
        this.author = author;
        this.authorNickname = authorNickname;
        this.content = content;
        this.parentComment = parentComment;
        this.replies = replies != null ? replies : new ArrayList<>();
        this.createdAt = createdAt;
        this.isUpdated = isUpdated;
    }

    public void updateComment(String content) {
        this.content = content;
        this.isUpdated = true;
    }
}

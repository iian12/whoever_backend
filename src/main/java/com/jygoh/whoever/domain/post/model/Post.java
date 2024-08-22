package com.jygoh.whoever.domain.post.model;

import com.jygoh.whoever.domain.comment.model.Comment;
import com.jygoh.whoever.domain.hashtag.model.Hashtag;
import com.jygoh.whoever.domain.member.entity.Member;
import jakarta.persistence.*;

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
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    private String authorNickname;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    private int viewCount;
    private int commentCount;
    private int likeCount;
    private int dislikeCount;

    @ManyToMany
    @JoinTable(
            name = "PostHashtag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private List<Hashtag> hashtags = new ArrayList<>();

    @Builder
    public Post(String title, String content, Member author,
                String authorNickname, LocalDateTime createdAt,
                LocalDateTime updatedAt, List<Comment> comments,
                List<Hashtag> hashtags, int viewCount, int likeCount, int dislikeCount) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.authorNickname = authorNickname;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
        this.comments = comments != null ? comments : new ArrayList<>();
        this.hashtags = hashtags != null ? hashtags : new ArrayList<>();
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void incrementCommentCount() {
        this.commentCount++;
    }

    public void decrementCommentCount() {
        this.commentCount--;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        this.likeCount--;
    }

    public void updatePost(String title, String content, List<Hashtag> hashtags) {
        this.title = title;
        this.content = content;
        this.hashtags = hashtags;
        this.updatedAt = LocalDateTime.now(); // 수정 시간 업데이트
    }
}
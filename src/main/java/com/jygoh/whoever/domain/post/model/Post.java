package com.jygoh.whoever.domain.post.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = false)
    private Long authorId;

    private String authorNickname;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String thumbnailUrl;

    @ElementCollection
    @CollectionTable(name = "PostHashtags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "hashtag_id")
    private List<Long> commentIds = new ArrayList<>();

    private int viewCount;
    private int commentCount;
    private int likeCount;

    @ElementCollection
    @CollectionTable(name = "PostHashtags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "hashtag_id")
    private List<Long> hashtagIds = new ArrayList<>();

    @Builder
    public Post(String title, String content, Long authorId, String authorNickname,
        String thumbnailUrl, LocalDateTime createdAt, LocalDateTime updatedAt,
        List<Long> commentIds, List<Long> hashtagIds, int viewCount, int likeCount) {
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
        this.commentIds = commentIds != null ? commentIds : new ArrayList<>();
        this.hashtagIds = hashtagIds != null ? hashtagIds : new ArrayList<>();
        this.viewCount = viewCount;
        this.likeCount = likeCount;
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

    public void updatePost(String title, String content, String thumbnailUrl,
        List<Long> hashtagIds) {
        this.title = title;
        this.content = content;
        this.hashtagIds = hashtagIds;
        this.thumbnailUrl = thumbnailUrl;
        this.updatedAt = LocalDateTime.now(); // 수정 시간 업데이트
    }
}
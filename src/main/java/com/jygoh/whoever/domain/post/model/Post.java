package com.jygoh.whoever.domain.post.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long authorId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String thumbnailUrl;

    @ElementCollection
    private List<Long> commentIds = new ArrayList<>();

    private int viewCount;
    private int commentCount;
    private int likeCount;

    @ElementCollection
    private List<Long> hashtagIds = new ArrayList<>();

    private Long categoryId;

    @Builder
    public Post(String title, String content, Long authorId, String thumbnailUrl,
        LocalDateTime createdAt, LocalDateTime updatedAt, List<Long> commentIds,
        List<Long> hashtagIds, int viewCount, int likeCount, Long categoryId) {
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.commentIds = commentIds != null ? commentIds : new ArrayList<>();
        this.hashtagIds = hashtagIds != null ? hashtagIds : new ArrayList<>();
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.categoryId = categoryId;
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

    public void updatePost(String title, String content, String thumbnailUrl, List<Long> hashtagIds,
        Long categoryId) {
        this.title = title;
        this.content = content;
        this.hashtagIds = hashtagIds;
        this.thumbnailUrl = thumbnailUrl;
        this.updatedAt = LocalDateTime.now(); // 수정 시간 업데이트
        this.categoryId = categoryId;
    }
}
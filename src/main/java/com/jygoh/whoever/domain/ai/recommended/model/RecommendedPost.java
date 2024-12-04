package com.jygoh.whoever.domain.ai.recommended.model;

import com.jygoh.whoever.domain.user.entity.Users;
import com.jygoh.whoever.domain.post.model.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendedPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private Double score;

    private LocalDateTime recommendedAt;

    @Builder
    public RecommendedPost(Users users, Post post, Double score, LocalDateTime recommendedAt) {
        this.users = users;
        this.post = post;
        this.score = score;
        this.recommendedAt = recommendedAt != null ? recommendedAt : LocalDateTime.now();
    }
}

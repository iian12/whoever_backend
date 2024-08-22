package com.jygoh.whoever.domain.ai.recommended.model;

import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.post.model.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendedPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private Double score;

    private LocalDateTime recommendedAt;

    @Builder
    public RecommendedPost(Member member, Post post, Double score, LocalDateTime recommendedAt) {
        this.member = member;
        this.post = post;
        this.score = score;
        this.recommendedAt = recommendedAt != null ? recommendedAt : LocalDateTime.now();
    }
}

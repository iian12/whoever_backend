package com.jygoh.whoever.domain.follow.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @EmbeddedId
    private FollowId id;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Follow(Long followerId, Long followeeId, LocalDateTime createdAt) {
        this.id = new FollowId(followerId, followeeId);
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public Long getFollowerId() {
        return this.id.getFollowerId();
    }

    public Long getFolloweeId() {
        return this.id.getFolloweeId();
    }
}

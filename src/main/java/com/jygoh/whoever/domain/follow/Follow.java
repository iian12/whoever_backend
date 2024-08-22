package com.jygoh.whoever.domain.follow;

import com.jygoh.whoever.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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

    @ManyToOne
    @MapsId("followerId")
    @JoinColumn(name = "follower_id")
    private Member follower;

    @ManyToOne
    @MapsId("followeeId")
    @JoinColumn(name = "followee_id")
    private Member followee;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Follow(Member follower, Member followee, LocalDateTime createdAt) {
        this.follower = follower;
        this.followee = followee;
        this.id = new FollowId(follower.getId(), followee.getId());
        this.createdAt = LocalDateTime.now();
    }

}

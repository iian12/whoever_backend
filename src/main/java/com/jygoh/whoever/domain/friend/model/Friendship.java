package com.jygoh.whoever.domain.friend.model;

import com.jygoh.whoever.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member1_id", nullable = false)
    private Member member1;

    @ManyToOne
    @JoinColumn(name = "member2_id", nullable = false)
    private Member member2;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Friendship(Member member1, Member member2, LocalDateTime createdAt) {
        this.member1 = member1;
        this.member2 = member2;
        this.createdAt = createdAt;
    }
}

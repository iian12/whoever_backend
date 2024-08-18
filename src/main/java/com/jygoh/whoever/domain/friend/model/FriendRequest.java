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
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendRequestStatus status;

    @Builder
    public FriendRequest(Member sender, Member receiver, LocalDateTime createdAt, FriendRequestStatus status) {
        this.sender = sender;
        this.receiver = receiver;
        this.createdAt = createdAt;
        this.status = status;
    }

    public void accept() {
        if (this.status != FriendRequestStatus.PENDING) {
            throw new IllegalStateException("Request cannot be accepted");
        }
        this.status = FriendRequestStatus.ACCEPTED;
    }

    public void reject() {
        this.status = FriendRequestStatus.REJECTED;
    }
}

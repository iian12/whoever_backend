package com.jygoh.whoever.domain.friend.repository;

import com.jygoh.whoever.domain.friend.model.FriendRequest;
import com.jygoh.whoever.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    boolean existsBySenderAndReceiver(Member sender, Member receiver);
}

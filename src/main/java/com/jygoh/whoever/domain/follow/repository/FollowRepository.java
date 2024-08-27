package com.jygoh.whoever.domain.follow.repository;

import com.jygoh.whoever.domain.follow.model.Follow;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    // Check if a follow relationship exists between a follower and followee
    Optional<Follow> findById_FollowerIdAndId_FolloweeId(Long followerId, Long followeeId);

    // Find all follow relationships where the member is the follower
    List<Follow> findById_FollowerId(Long followerId);

    // Find all follow relationships where the member is the followee
    List<Follow> findById_FolloweeId(Long followeeId);


}

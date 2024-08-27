package com.jygoh.whoever.domain.follow.service;

import com.jygoh.whoever.domain.follow.model.Follow;
import com.jygoh.whoever.domain.follow.repository.FollowRepository;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FollowServiceImpl implements FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public FollowServiceImpl(MemberRepository memberRepository, FollowRepository followRepository,
        JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.followRepository = followRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void toggleFollow(String token, Long followeeId) {

        Long followerId = jwtTokenProvider.getMemberIdFromToken(token);

        // Validate that a user cannot follow themselves
        if (followerId.equals(followeeId)) {
            throw new IllegalArgumentException("A user cannot follow themselves.");
        }

        // Check if the follow relationship already exists
        Optional<Follow> existingFollow = followRepository.findById_FollowerIdAndId_FolloweeId(followerId, followeeId);

        if (existingFollow.isPresent()) {
            // If follow relationship exists, delete it and decrease follower count
            followRepository.delete(existingFollow.get());
            // Assuming `followeeId` refers to the followee's member ID
            memberRepository.findById(followeeId).ifPresent(Member::decreaseFollowerCount);
        } else {
            // If follow relationship does not exist, create it and increase follower count
            Follow follow = Follow.builder()
                    .followerId(followerId)
                    .followeeId(followeeId)
                    .createdAt(LocalDateTime.now())
                    .build();
            followRepository.save(follow);
            memberRepository.findById(followeeId).ifPresent(Member::increaseFollowerCount);
        }
    }

}

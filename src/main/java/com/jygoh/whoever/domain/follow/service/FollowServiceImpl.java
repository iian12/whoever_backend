package com.jygoh.whoever.domain.follow.service;

import com.jygoh.whoever.domain.follow.model.Follow;
import com.jygoh.whoever.domain.follow.repository.FollowRepository;
import com.jygoh.whoever.domain.follow.dto.FollowResponseDto;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
        Member follower = memberRepository.findById(followerId).orElseThrow();
        Member followee = memberRepository.findById(followeeId).orElseThrow();

        if (follower.equals(followee)) {
            throw new IllegalArgumentException("A user cannot follow themselves.");
        }

        Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowee(follower, followee);
        if (existingFollow.isPresent()) {

            followRepository.delete(existingFollow.get());
            followee.decreaseFollowerCount();
        } else {

            Follow follow = Follow.builder()
                .follower(follower)
                .followee(followee)
                .createdAt(LocalDateTime.now())
                .build();
            followRepository.save(follow);
            followee.increaseFollowerCount();
        }
    }

    @Override
    public List<FollowResponseDto> getFollowing(String token) {

        Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
        Member member = memberRepository.findById(memberId).orElseThrow();

        return member.getFollowing().stream()
            .map(follow -> new FollowResponseDto(follow.getFollowee()))
            .collect(Collectors.toList());
    }

    @Override
    public int getFollowerCount(Long memberId) {

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        return member.getFollowers().size();
    }
}

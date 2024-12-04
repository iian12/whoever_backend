package com.jygoh.whoever.domain.follow.service;

import com.jygoh.whoever.domain.follow.model.Follow;
import com.jygoh.whoever.domain.follow.repository.FollowRepository;
import com.jygoh.whoever.domain.user.entity.Users;
import com.jygoh.whoever.domain.user.repository.UserRepository;
import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FollowServiceImpl implements FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public FollowServiceImpl(UserRepository userRepository, FollowRepository followRepository,
        JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void toggleFollow(String token, Long followeeId) {
        Long followerId = jwtTokenProvider.getUserIdFromToken(token);
        if (followerId.equals(followeeId)) {
            throw new IllegalArgumentException("A user cannot follow themselves.");
        }
        Optional<Follow> existingFollow = followRepository.findById_FollowerIdAndId_FolloweeId(
            followerId, followeeId);
        if (existingFollow.isPresent()) {
            followRepository.delete(existingFollow.get());
            userRepository.findById(followeeId).ifPresent(Users::decreaseFollowerCount);
        } else {
            Follow follow = Follow.builder().followerId(followerId).followeeId(followeeId)
                .createdAt(LocalDateTime.now()).build();
            followRepository.save(follow);
            userRepository.findById(followeeId).ifPresent(Users::increaseFollowerCount);
        }
    }

}

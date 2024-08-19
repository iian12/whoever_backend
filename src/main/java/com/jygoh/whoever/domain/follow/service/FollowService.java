package com.jygoh.whoever.domain.follow.service;

import com.jygoh.whoever.domain.follow.FollowResponseDto;
import java.util.List;

public interface FollowService {

    void follow(String token, Long followeeId);

    void unfollow(String token, Long followeeId);

    List<FollowResponseDto> getFollowing(String token);
}

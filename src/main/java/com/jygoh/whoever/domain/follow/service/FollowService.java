package com.jygoh.whoever.domain.follow.service;

import com.jygoh.whoever.domain.follow.FollowResponseDto;
import java.util.List;

public interface FollowService {

    void toggleFollow(String token, Long followeeId);

    List<FollowResponseDto> getFollowing(String token);

    int getFollowerCount(Long memberId);
}

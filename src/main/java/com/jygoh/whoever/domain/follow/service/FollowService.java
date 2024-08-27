package com.jygoh.whoever.domain.follow.service;

public interface FollowService {

    void toggleFollow(String token, Long followeeId);

}

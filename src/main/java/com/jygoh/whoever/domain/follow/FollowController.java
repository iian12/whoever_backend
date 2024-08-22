package com.jygoh.whoever.domain.follow;

import com.jygoh.whoever.domain.follow.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/follow")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/follow/{followeeId}")
    public ResponseEntity<Void> follow(@RequestHeader("Authorization") String token, @PathVariable Long followeeId) {

        followService.toggleFollow(token, followeeId);

        return ResponseEntity.ok().build();
    }
}

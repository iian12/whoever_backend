package com.jygoh.whoever.domain.follow;

import com.jygoh.whoever.domain.follow.service.FollowService;
import com.jygoh.whoever.global.security.jwt.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/follow")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/follow")
    public ResponseEntity<Void> follow(HttpServletRequest request, @RequestParam Long followeeId) {

        String token = TokenUtils.extractTokenFromRequest(request);

        followService.follow(token, followeeId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Void> unfollow(HttpServletRequest request, @RequestParam Long followeeId) {

        String token = TokenUtils.extractTokenFromRequest(request);

        followService.unfollow(token, followeeId);

        return ResponseEntity.ok().build();
    }
}

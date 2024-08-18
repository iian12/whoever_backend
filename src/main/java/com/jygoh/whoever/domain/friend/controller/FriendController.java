package com.jygoh.whoever.domain.friend.controller;

import com.jygoh.whoever.domain.friend.service.FriendRequestService;
import com.jygoh.whoever.domain.friend.model.Friendship;
import com.jygoh.whoever.global.security.jwt.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friend-requests")
public class FriendController {

    private final FriendRequestService friendRequestService;

    public FriendController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendFriendRequest(@RequestParam Long receiverId, HttpServletRequest request) {

        String token = TokenUtils.extractTokenFromRequest(request);

        friendRequestService.sendFriendRequest(token, receiverId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> acceptFriendRequest(@RequestParam Long requestId, HttpServletRequest request) {
        String token = TokenUtils.extractTokenFromRequest(request);

        friendRequestService.acceptFriendRequest(token, requestId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject")
    public ResponseEntity<Void> rejectFriendRequest(@RequestParam Long requestId, HttpServletRequest request) {

        String token = TokenUtils.extractTokenFromRequest(request);

        friendRequestService.rejectFriendRequest(token, requestId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<Void> cancelFriendRequest(@RequestParam Long requestId, HttpServletRequest request) {

        String token = TokenUtils.extractTokenFromRequest(request);

        friendRequestService.cancelFriendRequest(token, requestId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/friends")
    public ResponseEntity<List<Friendship>> getFriendsList(HttpServletRequest request) {

        String token = TokenUtils.extractTokenFromRequest(request);

        List<Friendship> friendsList = friendRequestService.getFriendsList(token);

        return ResponseEntity.ok(friendsList);
    }
}

package com.jygoh.whoever.domain.post.controller;

import com.jygoh.whoever.domain.post.dto.PostCreateRequestDto;
import com.jygoh.whoever.domain.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new IllegalArgumentException("Invalid or missing Authorization header");
    }

    @PostMapping
    public ResponseEntity<Long> createPost(@RequestBody PostCreateRequestDto requestDto, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);

        Long postId = postService.createPost(requestDto, token);

        return ResponseEntity.ok(postId);
    }
}

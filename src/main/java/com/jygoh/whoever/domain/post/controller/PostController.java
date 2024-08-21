package com.jygoh.whoever.domain.post.controller;

import com.jygoh.whoever.domain.post.dto.PostCreateRequestDto;
import com.jygoh.whoever.domain.post.dto.PostListResponseDto;
import com.jygoh.whoever.domain.post.service.PostService;
import com.jygoh.whoever.global.security.jwt.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Long> createPost(@RequestBody PostCreateRequestDto requestDto, HttpServletRequest request) {
        String token = TokenUtils.extractTokenFromRequest(request);

        Long postId = postService.createPost(requestDto, token);

        return ResponseEntity.ok(postId);
    }

    @GetMapping
    public List<PostListResponseDto> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return postService.getAllPosts(page, size);
    }
}

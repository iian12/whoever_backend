package com.jygoh.whoever.domain.post.controller;

import com.jygoh.whoever.domain.post.dto.PostCreateRequestDto;
import com.jygoh.whoever.domain.post.dto.PostDetailResponseDto;
import com.jygoh.whoever.domain.post.dto.PostListResponseDto;
import com.jygoh.whoever.domain.post.service.PostService;
import com.jygoh.whoever.global.security.jwt.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponseDto> getPost(
        @PathVariable Long postId,
        @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            PostDetailResponseDto postDetails = postService.getPostDetail(postId, token);
            return ResponseEntity.ok(postDetails);
        } catch (Exception e) {
            // 로그를 남기거나 사용자에게 적절한 오류 메시지를 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> toggleLike(
        @PathVariable Long postId, @RequestHeader("Authorization") String token) {
        postService.toggleLike(postId, token);
        return ResponseEntity.ok().build();
    }
}

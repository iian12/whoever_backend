package com.jygoh.whoever.domain.post.controller;

import com.jygoh.whoever.domain.member.repository.MemberRepository;
import com.jygoh.whoever.domain.post.dto.PostCreateRequestDto;
import com.jygoh.whoever.domain.post.dto.PostDetailResponseDto;
import com.jygoh.whoever.domain.post.dto.PostListResponseDto;
import com.jygoh.whoever.domain.post.service.PostService;
import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import com.jygoh.whoever.global.security.jwt.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;
    private final MemberRepository memberRepository;

    public PostController(PostService postService, JwtTokenProvider jwtTokenProvider,
        MemberRepository memberRepository) {
        this.postService = postService;
        this.memberRepository = memberRepository;
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequestDto requestDto,
        HttpServletRequest request) {
        String token = TokenUtils.extractTokenFromRequest(request);
        if (requestDto.getTitle() == null || requestDto.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Title cannot be empty.");
        }
        if (requestDto.getContent() == null || requestDto.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Content cannot be empty.");
        }
        Long postId = postService.createPost(requestDto, token);
        return ResponseEntity.ok(postId);
    }

    @GetMapping
    public List<PostListResponseDto> getAllPosts(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        return postService.getAllPosts(page, size);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponseDto> getPost(@PathVariable Long postId,
        HttpServletRequest request) {
        try {
            String token = TokenUtils.extractTokenFromRequest(request);
            log.info(token);
            PostDetailResponseDto postDetails = postService.getPostDetail(postId, token);
            return ResponseEntity.ok(postDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> toggleLike(@PathVariable Long postId, HttpServletRequest request) {
        String token = TokenUtils.extractTokenFromRequest(request);
        postService.toggleLike(postId, token);
        return ResponseEntity.ok().build();
    }
}

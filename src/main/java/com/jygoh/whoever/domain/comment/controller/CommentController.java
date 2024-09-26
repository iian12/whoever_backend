package com.jygoh.whoever.domain.comment.controller;

import com.jygoh.whoever.domain.comment.dto.CommentCreateRequestDto;
import com.jygoh.whoever.domain.comment.dto.CommentUpdateRequestDto;
import com.jygoh.whoever.domain.comment.service.CommentService;
import com.jygoh.whoever.global.security.jwt.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Long> createComment(@RequestBody CommentCreateRequestDto requestDto,
        HttpServletRequest request) {
        String token = TokenUtils.extractTokenFromRequest(request);
        Long commentId = commentService.createComment(requestDto, token);
        return ResponseEntity.ok(commentId);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Long> updateComment(@PathVariable Long commentId,
        @RequestBody CommentUpdateRequestDto requestDto, HttpServletRequest request) {
        String token = TokenUtils.extractTokenFromRequest(request);
        Long comment = commentService.updateComment(commentId, requestDto, token);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
        HttpServletRequest request) {
        String token = TokenUtils.extractTokenFromRequest(request);
        commentService.deleteComment(commentId, token);
        return ResponseEntity.noContent().build();
    }

}

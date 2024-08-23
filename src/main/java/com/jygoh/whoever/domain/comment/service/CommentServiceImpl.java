package com.jygoh.whoever.domain.comment.service;

import com.jygoh.whoever.domain.comment.dto.CommentCreateRequestDto;
import com.jygoh.whoever.domain.comment.model.Comment;
import com.jygoh.whoever.domain.comment.repository.CommentRepository;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.post.model.Post;
import com.jygoh.whoever.domain.post.repository.PostRepository;
import com.jygoh.whoever.global.auth.CustomUserDetails;
import com.jygoh.whoever.global.auth.CustomUserDetailsService;
import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,
        JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public Long createComment(CommentCreateRequestDto requestDto, String token) {

        Long memberId = jwtTokenProvider.getMemberIdFromToken(token);

        UserDetails userDetails = customUserDetailsService.loadUserById(memberId);

        Member author = ((CustomUserDetails) userDetails).getMember();

        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        Comment parentComment = null;
        if (requestDto.getParentCommentId() != null) {
            parentComment = commentRepository.findById(requestDto.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid parent comment ID"));
        }

        Comment comment = requestDto.toEntity(post, author, author.getNickname(), parentComment);

        commentRepository.save(comment);

        post.incrementCommentCount();
        postRepository.save(post);

        return comment.getId();
    }
}

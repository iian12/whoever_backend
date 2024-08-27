package com.jygoh.whoever.domain.comment.service;

import com.jygoh.whoever.domain.comment.dto.CommentCreateRequestDto;
import com.jygoh.whoever.domain.comment.dto.CommentUpdateRequestDto;
import com.jygoh.whoever.domain.comment.model.Comment;
import com.jygoh.whoever.domain.comment.repository.CommentRepository;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import com.jygoh.whoever.domain.post.model.Post;
import com.jygoh.whoever.domain.post.repository.PostRepository;
import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,
        JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    public Long createComment(CommentCreateRequestDto requestDto, String token) {

        Long memberId = jwtTokenProvider.getMemberIdFromToken(token);

        Member author = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        Comment parentComment = null;
        if (requestDto.getParentCommentId() != null) {
            parentComment = commentRepository.findById(requestDto.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid parent comment ID"));
        }

        Comment comment = requestDto.toEntity(post, author);

        commentRepository.save(comment);
        post.incrementCommentCount();
        postRepository.save(post);

        return comment.getId();
    }

    @Override
    public Long updateComment(Long commentId, CommentUpdateRequestDto requestDto, String token) {
        Long memberId = jwtTokenProvider.getMemberIdFromToken(token);

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));

        if (!comment.getAuthorId().equals(memberId)) {
            throw new AccessDeniedException("You do not have permission to edit this post.");
        }

        comment.updateComment(requestDto.getContent());

        commentRepository.save(comment);

        return comment.getId();
    }


}

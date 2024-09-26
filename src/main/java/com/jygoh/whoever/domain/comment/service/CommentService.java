package com.jygoh.whoever.domain.comment.service;

import com.jygoh.whoever.domain.comment.dto.CommentCreateRequestDto;
import com.jygoh.whoever.domain.comment.dto.CommentUpdateRequestDto;

public interface CommentService {

    Long createComment(CommentCreateRequestDto requestDto, String token);

    Long updateComment(Long commentId, CommentUpdateRequestDto requestDto, String token);

    void deleteComment(Long commentId, String token);
}

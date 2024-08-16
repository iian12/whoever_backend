package com.jygoh.whoever.domain.comment.service;

import com.jygoh.whoever.domain.comment.dto.CommentCreateRequestDto;

public interface CommentService {

    Long createComment(CommentCreateRequestDto requestDto, String token);
}

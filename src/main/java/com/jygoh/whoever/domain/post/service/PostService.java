package com.jygoh.whoever.domain.post.service;

import com.jygoh.whoever.domain.post.dto.PostCreateRequestDto;
import com.jygoh.whoever.domain.post.dto.PostUpdateRequestDto;

public interface PostService {

    Long createPost(PostCreateRequestDto requestDto);

    void updatePost(Long postId, PostUpdateRequestDto requestDto);

    void deletePost(Long postId);
}

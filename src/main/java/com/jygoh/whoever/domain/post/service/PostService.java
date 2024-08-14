package com.jygoh.whoever.domain.post.service;

import com.jygoh.whoever.domain.post.dto.PostCreateRequestDto;
import com.jygoh.whoever.domain.post.dto.PostDetailResponseDto;
import com.jygoh.whoever.domain.post.dto.PostListResponseDto;
import com.jygoh.whoever.domain.post.dto.PostUpdateRequestDto;

import java.util.List;

public interface PostService {

    Long createPost(PostCreateRequestDto requestDto, String token);

    void updatePost(Long postId, PostUpdateRequestDto requestDto);

    void deletePost(Long postId);

    List<PostListResponseDto> getAllPosts();

    PostDetailResponseDto getPostDetail(Long postId);
}

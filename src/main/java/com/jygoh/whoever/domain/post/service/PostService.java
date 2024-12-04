package com.jygoh.whoever.domain.post.service;

import com.jygoh.whoever.domain.post.dto.PostCreateRequestDto;
import com.jygoh.whoever.domain.post.dto.PostDetailResponseDto;
import com.jygoh.whoever.domain.post.dto.PostListResponseDto;
import com.jygoh.whoever.domain.post.dto.PostUpdateRequestDto;
import java.util.List;

public interface PostService {

    Long createPost(PostCreateRequestDto requestDto, String token);

    Long updatePost(Long postId, PostUpdateRequestDto requestDto, String token);

    void deletePost(Long postId, String token);

    List<PostListResponseDto> getAllPosts(int page, int size);

    List<PostListResponseDto> getPostsByCategory(Long categoryId);

    PostDetailResponseDto getPostDetail(Long postId, String token);

    void toggleLike(Long postId, String token);
}

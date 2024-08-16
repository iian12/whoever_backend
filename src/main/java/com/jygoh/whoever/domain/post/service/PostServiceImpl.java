package com.jygoh.whoever.domain.post.service;

import com.jygoh.whoever.domain.hashtag.model.Hashtag;
import com.jygoh.whoever.domain.hashtag.service.HashtagService;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.post.dto.PostCreateRequestDto;
import com.jygoh.whoever.domain.post.dto.PostDetailResponseDto;
import com.jygoh.whoever.domain.post.dto.PostListResponseDto;
import com.jygoh.whoever.domain.post.dto.PostUpdateRequestDto;
import com.jygoh.whoever.domain.post.model.Post;
import com.jygoh.whoever.domain.post.repository.PostRepository;
import com.jygoh.whoever.global.auth.CustomUserDetails;
import com.jygoh.whoever.global.auth.CustomUserDetailsService;
import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final HashtagService hashtagService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public PostServiceImpl(PostRepository postRepository,
         HashtagService hashtagService, JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.postRepository = postRepository;
        this.hashtagService = hashtagService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public Long createPost(PostCreateRequestDto requestDto, String token) {

       Long userId = jwtTokenProvider.getUserIdFromToken(token);

       UserDetails userDetails = customUserDetailsService.loadUserById(userId);

       Member author = ((CustomUserDetails) userDetails).getMember();

       List<Hashtag> hashtags = hashtagService.findOrCreateHashtags(requestDto.getHashtagNames());

       Post post = requestDto.toEntity(author, author.getNickname(), hashtags);

       postRepository.save(post);

       return post.getId();
    }

    @Override
    public void updatePost(Long postId, PostUpdateRequestDto requestDto) {

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        List<Hashtag> hashtags = hashtagService.findOrCreateHashtags(requestDto.getHashtagNames());

        // Post 엔티티의 도메인 메서드를 사용하여 업데이트
        post.update(requestDto.getTitle(), requestDto.getContent(), hashtags);

        postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId) {

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        postRepository.delete(post);
    }

    @Override
    public List<PostListResponseDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public PostDetailResponseDto getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return new PostDetailResponseDto(post);
    }
}

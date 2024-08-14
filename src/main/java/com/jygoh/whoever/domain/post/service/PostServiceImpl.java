package com.jygoh.whoever.domain.post.service;

import com.jygoh.whoever.domain.hashtag.model.Hashtag;
import com.jygoh.whoever.domain.hashtag.repository.HashtagRepository;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import com.jygoh.whoever.domain.post.dto.PostCreateRequestDto;
import com.jygoh.whoever.domain.post.dto.PostDetailResponseDto;
import com.jygoh.whoever.domain.post.dto.PostListResponseDto;
import com.jygoh.whoever.domain.post.dto.PostUpdateRequestDto;
import com.jygoh.whoever.domain.post.model.Post;
import com.jygoh.whoever.domain.post.repository.PostRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final HashtagRepository hashtagRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public PostServiceImpl(PostRepository postRepository, MemberRepository memberRepository,
        HashtagRepository hashtagRepository, JwtTokenProvider jwtTokenProvider) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.hashtagRepository = hashtagRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Long createPost(PostCreateRequestDto requestDto, String token) {

        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        Member author = memberRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid author ID"));

        List<Hashtag> hashtags = hashtagRepository.findAllById(requestDto.getHashtagIds());

        Post post = requestDto.toEntity(author, hashtags);

        postRepository.save(post);

        return post.getId();
    }

    @Override
    public void updatePost(Long postId, PostUpdateRequestDto requestDto) {

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        List<Hashtag> hashtags = hashtagRepository.findAllById(requestDto.getHashtagIds());

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

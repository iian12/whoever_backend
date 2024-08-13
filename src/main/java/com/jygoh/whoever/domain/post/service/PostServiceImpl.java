package com.jygoh.whoever.domain.post.service;

import com.jygoh.whoever.domain.hashtag.model.Hashtag;
import com.jygoh.whoever.domain.hashtag.repository.HashtagRepository;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import com.jygoh.whoever.domain.post.dto.PostCreateRequestDto;
import com.jygoh.whoever.domain.post.dto.PostUpdateRequestDto;
import com.jygoh.whoever.domain.post.model.Post;
import com.jygoh.whoever.domain.post.repository.PostRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final HashtagRepository hashtagRepository;


    public PostServiceImpl(PostRepository postRepository,
        MemberRepository memberRepository,
        HashtagRepository hashtagRepository) {

        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.hashtagRepository = hashtagRepository;
    }

    @Override
    public Long createPost(PostCreateRequestDto requestDto) {

        Member author = memberRepository.findById(requestDto.getAuthorId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid author ID"));

        List<Hashtag> hashtags = hashtagRepository.findAllById(requestDto.getHashtagIds());

        Post post = Post.builder()
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .author(author)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .hashtags(hashtags)
            .build();

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
}

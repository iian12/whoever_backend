package com.jygoh.whoever.domain.member.dto;

import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.post.repository.PostRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemberProfileResponseDtoFactory {

    private final PostRepository postRepository;

    public MemberProfileResponseDtoFactory(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public MemberProfileResponseDto createFromMember(Member member) {
        // 작성한 글의 제목을 조회
        List<MemberProfileResponseDto.PostForProfileDto> posts = postRepository.findAllByAuthorId(member.getId())
                .stream()
                .map(post -> new MemberProfileResponseDto.PostForProfileDto(post.getId(), post.getTitle()))
                .collect(Collectors.toList());

        int followerCount = member.getFollowerCount();

        return MemberProfileResponseDto.builder()
                .nickname(member.getNickname())
                .posts(posts)
                .followerCount(followerCount)
                .build();
    }
}

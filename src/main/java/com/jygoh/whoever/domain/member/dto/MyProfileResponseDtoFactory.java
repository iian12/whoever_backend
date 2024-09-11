package com.jygoh.whoever.domain.member.dto;

import com.jygoh.whoever.domain.comment.repository.CommentRepository;
import com.jygoh.whoever.domain.follow.dto.FollowResponseDto;
import com.jygoh.whoever.domain.follow.repository.FollowRepository;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import com.jygoh.whoever.domain.post.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MyProfileResponseDtoFactory {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    public MyProfileResponseDtoFactory(PostRepository postRepository,
        CommentRepository commentRepository, FollowRepository followRepository,
        MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.followRepository = followRepository;
        this.memberRepository = memberRepository;
    }

    public MyProfileResponseDto createFromMember(Member member) {
        // 작성한 게시글 조회
        List<MyProfileResponseDto.PostForProfileDto> posts = postRepository.findAllByAuthorId(
                member.getId()).stream()
            .map(post -> new MyProfileResponseDto.PostForProfileDto(post.getId(), post.getTitle()))
            .collect(Collectors.toList());
        // 작성한 댓글 조회
        List<MyProfileResponseDto.CommentForProfileDto> comments = commentRepository.findByAuthorId(
            member.getId()).stream().map(
            comment -> new MyProfileResponseDto.CommentForProfileDto(comment.getId(),
                comment.getContent(),
                new MyProfileResponseDto.CommentForProfileDto.PostForCommentDto(comment.getPostId(),
                    ""  // Assuming post title is not needed for comments
                ))).collect(Collectors.toList());
        // 팔로우하는 사용자 조회
        List<FollowResponseDto> following = followRepository.findById_FollowerId(member.getId())
            .stream().map(follow -> {
                Member followee = memberRepository.findById(follow.getFolloweeId())
                    .orElseThrow(() -> new IllegalArgumentException("Followee not found"));
                return new FollowResponseDto(followee.getId(), followee.getNickname());
            }).collect(Collectors.toList());
        // 팔로워 수
        int followerCount = member.getFollowerCount();
        return MyProfileResponseDto.builder()
            .email(member.getEmail()).nickname(member.getNickname())
            .role(member.getRole().name())  // Assuming Role is an Enum
            .posts(posts).comments(comments).following(following).followerCount(followerCount)
            .build();
    }
}

package com.jygoh.whoever.domain.member.dto;

import com.jygoh.whoever.domain.member.entity.Member;
import java.util.stream.Collectors;

public class MyProfileResponseDtoFactory {

    public static MyProfileResponseDto createFromMember(Member member) {
        return MyProfileResponseDto.builder()
            .username(member.getUsername())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .role(member.getRole().name())
            .posts(member.getPosts().stream()
                .map(MyProfileResponseDto.PostForProfileDto::new)
                .collect(Collectors.toList()))
            .comments(member.getComments().stream()
                .map(MyProfileResponseDto.CommentForProfileDto::new)
                .collect(Collectors.toList()))
            .following(member.getFollowing().stream()
                .map(follow -> new MyProfileResponseDto.FollowForProfileDto(follow.getFollowee()))
                .collect(Collectors.toList()))
            .followerCount(member.getFollowers().size())
            .build();
    }
}

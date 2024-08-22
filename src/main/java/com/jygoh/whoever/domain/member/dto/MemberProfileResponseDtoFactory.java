package com.jygoh.whoever.domain.member.dto;

import com.jygoh.whoever.domain.member.dto.MemberProfileResponseDto.PostForProfileDto;
import com.jygoh.whoever.domain.member.entity.Member;
import java.util.List;
import java.util.stream.Collectors;

public class MemberProfileResponseDtoFactory {

    public static MemberProfileResponseDto createFromMember(Member member) {
        List<PostForProfileDto> posts = member.getPosts().stream()
            .map(MemberProfileResponseDto.PostForProfileDto::new)
            .collect(Collectors.toList());

        List<MemberProfileResponseDto.CommentForProfileDto> comments = member.getComments().stream()
            .map(MemberProfileResponseDto.CommentForProfileDto::new)
            .collect(Collectors.toList());

        int followerCount = member.getFollowers().size();

        return MemberProfileResponseDto.builder()
            .nickname(member.getNickname())
            .posts(posts)
            .comments(comments)
            .followerCount(followerCount)
            .build();
    }

}

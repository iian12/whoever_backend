package com.jygoh.whoever.domain.member.profile.service;

import com.jygoh.whoever.domain.member.profile.dto.MyBasicInfoResponseDto;
import com.jygoh.whoever.domain.member.profile.dto.MyCommentsResponseDto;
import com.jygoh.whoever.domain.member.profile.dto.MyLikedPostsResponseDto;
import com.jygoh.whoever.domain.member.profile.dto.MyPostsResponseDto;

public interface ProfileService {

    MyBasicInfoResponseDto getMyProfile(String token);

    MyPostsResponseDto getMyPosts(String token);

    MyCommentsResponseDto getMyComments(String token);

    MyLikedPostsResponseDto getMyLikedPosts(String token);
}

package com.jygoh.whoever.domain.user.profile.service;

import com.jygoh.whoever.domain.user.profile.dto.MyBasicInfoResponseDto;
import com.jygoh.whoever.domain.user.profile.dto.MyCommentsResponseDto;
import com.jygoh.whoever.domain.user.profile.dto.MyLikedPostsResponseDto;
import com.jygoh.whoever.domain.user.profile.dto.MyPostsResponseDto;

public interface ProfileService {

    MyBasicInfoResponseDto getMyProfile(String token);

    MyPostsResponseDto getMyPosts(String token);

    MyCommentsResponseDto getMyComments(String token);

    MyLikedPostsResponseDto getMyLikedPosts(String token);
}

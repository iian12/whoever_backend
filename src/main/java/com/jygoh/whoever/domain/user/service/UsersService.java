package com.jygoh.whoever.domain.user.service;

import com.jygoh.whoever.domain.user.dto.UserCreateReqDto;
import com.jygoh.whoever.domain.user.dto.UserUpdateReqDto;
import com.jygoh.whoever.domain.user.profile.dto.UserProfileResDto;

public interface UsersService {

    Long registerUser(UserCreateReqDto requestDto);

    void updateMember(Long id, UserUpdateReqDto requestDto);

    void deleteMember(Long id);

    UserProfileResDto getMemberProfileByNickname(String nickname);

    void setNickname(Long userId, String nickname);
}


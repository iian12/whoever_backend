package com.jygoh.whoever.domain.member.service;

import com.jygoh.whoever.domain.member.dto.MemberCreateRequestDto;
import com.jygoh.whoever.domain.member.dto.MemberProfileResponseDto;
import com.jygoh.whoever.domain.member.dto.MemberUpdateRequestDto;
import com.jygoh.whoever.domain.member.dto.MyProfileResponseDto;
import com.jygoh.whoever.domain.member.entity.Provider;

public interface MemberService {

    Long registerMember(MemberCreateRequestDto requestDto);

    Long processOAuth2User(String email, String name, Provider provider, String profileImageUrl, String providerId);

    void updateMember(Long id, MemberUpdateRequestDto requestDto);

    void deleteMember(Long id);

    MemberProfileResponseDto getMemberProfileByNickname(String nickname);

    MyProfileResponseDto getMyProfile(String token);
}


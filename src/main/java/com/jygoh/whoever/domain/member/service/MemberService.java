package com.jygoh.whoever.domain.member.service;

import com.jygoh.whoever.domain.member.dto.MemberCreateRequestDto;
import com.jygoh.whoever.domain.member.dto.MemberProfileResponseDto;
import com.jygoh.whoever.domain.member.dto.MemberUpdateRequestDto;
import com.jygoh.whoever.domain.member.dto.MyProfileResponseDto;

public interface MemberService {

    Long register(MemberCreateRequestDto requestDto);
    void updateMember(Long id, MemberUpdateRequestDto requestDto);
    void deleteMember(Long id);
    MemberProfileResponseDto getMemberProfileByNickname(String nickname);
    MyProfileResponseDto getMyProfile(String token);
}

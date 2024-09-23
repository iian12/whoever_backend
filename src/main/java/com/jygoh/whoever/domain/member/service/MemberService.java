package com.jygoh.whoever.domain.member.service;

import com.jygoh.whoever.domain.member.dto.MemberCreateRequestDto;
import com.jygoh.whoever.domain.member.dto.MemberUpdateRequestDto;
import com.jygoh.whoever.domain.member.profile.dto.MemberProfileResponseDto;

public interface MemberService {

    Long registerMember(MemberCreateRequestDto requestDto);

    void updateMember(Long id, MemberUpdateRequestDto requestDto);

    void deleteMember(Long id);

    MemberProfileResponseDto getMemberProfileByNickname(String nickname);

    void setNickname(Long memberId, String nickname);
}


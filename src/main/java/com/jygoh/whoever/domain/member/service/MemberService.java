package com.jygoh.whoever.domain.member.service;

import com.jygoh.whoever.domain.member.dto.MemberCreateRequestDto;
import com.jygoh.whoever.domain.member.dto.MemberResponseDto;
import com.jygoh.whoever.domain.member.dto.MemberUpdateRequestDto;
import com.jygoh.whoever.domain.member.dto.MyProfileResponseDto;

import java.util.List;

public interface MemberService {

    Long register(MemberCreateRequestDto requestDto);
    void updateMember(Long id, MemberUpdateRequestDto requestDto);
    void deleteMember(Long id);
    MemberResponseDto getMemberById(Long id);
    List<MemberResponseDto> getAllMembers();
    MyProfileResponseDto getMemberProfileById(Long id);
}

package com.jygoh.whoever.domain.member.service;

import com.jygoh.whoever.domain.member.dto.MemberCreateRequestDto;

public interface MemberService {

    void register(MemberCreateRequestDto requestDto);


}

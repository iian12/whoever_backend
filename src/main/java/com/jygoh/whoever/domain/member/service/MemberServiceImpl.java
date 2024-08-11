package com.jygoh.whoever.domain.member.service;

import com.jygoh.whoever.domain.member.dto.MemberCreateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {


    @Override
    public void register(MemberCreateRequestDto requestDto) {

    }
}

package com.jygoh.whoever.domain.member.service;

import com.jygoh.whoever.domain.member.dto.*;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberServiceImpl(MemberRepository memberRepository,
        BCryptPasswordEncoder passwordEncoder) {

        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Long register(MemberCreateRequestDto requestDto) {

        if (memberRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }

        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        Member member = requestDto.toEntity().toBuilder()
                .password(encodedPassword).build();

        memberRepository.save(member);
        return member.getId();
    }

    @Override
    public void updateMember(Long id, MemberUpdateRequestDto requestDto) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Member Id"));

        member.updateProfile(
            requestDto.getUsername(),
            requestDto.getEmail(),
            requestDto.getNickname(),
            passwordEncoder.encode(requestDto.getPassword())
        );
    }

    @Override
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        memberRepository.delete(member);
    }

    @Override
    public MemberResponseDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        return new MemberResponseDto(member);
    }

    @Override
    public List<MemberResponseDto> getAllMembers() {
        return memberRepository.findAll().stream()
            .map(MemberResponseDto::new)
            .collect(Collectors.toList());
    }

    @Override
    public MyProfileResponseDto getMemberProfileById(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        return new MyProfileResponseDto(member);
    }
}

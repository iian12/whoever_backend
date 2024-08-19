package com.jygoh.whoever.domain.member.service;

import com.jygoh.whoever.domain.member.dto.*;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;

    public MemberServiceImpl(MemberRepository memberRepository,
        BCryptPasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {

        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
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
    public MemberResponseDto getMemberProfileById(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        return new MemberResponseDto(member);
    }

    @Override
    public MyProfileResponseDto getMyProfile(String token) {
        Long memberId = jwtTokenProvider.getUserIdFromToken(token);
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return convertToDto(member);
    }

    private MyProfileResponseDto convertToDto(Member member) {
        return MyProfileResponseDto.builder()
            .id(member.getId())
            .username(member.getUsername())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .role(member.getRole().name())
            .posts(member.getPosts().stream()
                .map(MyProfileResponseDto.PostForProfileDto::new)
                .collect(Collectors.toList()))
            .comments(member.getComments().stream()
                .map(MyProfileResponseDto.CommentForProfileDto::new)
                .collect(Collectors.toList()))
            .following(member.getFollowing().stream()
                .map(follow -> new MyProfileResponseDto.FollowForProfileDto(follow.getFollowee()))
                .collect(Collectors.toList()))
            .build();
    }
}

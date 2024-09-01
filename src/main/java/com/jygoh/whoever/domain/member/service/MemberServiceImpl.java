package com.jygoh.whoever.domain.member.service;

import com.jygoh.whoever.domain.member.dto.MemberCreateRequestDto;
import com.jygoh.whoever.domain.member.dto.MemberProfileResponseDto;
import com.jygoh.whoever.domain.member.dto.MemberProfileResponseDtoFactory;
import com.jygoh.whoever.domain.member.dto.MemberUpdateRequestDto;
import com.jygoh.whoever.domain.member.dto.MyProfileResponseDto;
import com.jygoh.whoever.domain.member.dto.MyProfileResponseDtoFactory;
import com.jygoh.whoever.domain.member.dto.SocialLoginRequestDto;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberProfileResponseDtoFactory memberProfileResponseDtoFactory;
    private final MyProfileResponseDtoFactory myProfileResponseDtoFactory;

    public MemberServiceImpl(MemberRepository memberRepository,
        BCryptPasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider,
        MemberProfileResponseDtoFactory memberProfileResponseDtoFactory,
        MyProfileResponseDtoFactory myProfileResponseDtoFactory) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberProfileResponseDtoFactory = memberProfileResponseDtoFactory;
        this.myProfileResponseDtoFactory = myProfileResponseDtoFactory;
    }

    @Override
    public Long registerMember(MemberCreateRequestDto requestDto) {
        if (memberRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        Optional<Member> existingMember = memberRepository.findByEmail(requestDto.getEmail());
        if (existingMember.isPresent()) {
            Member member = existingMember.get();
            if (!member.getProviders().isEmpty()) {
                throw new IllegalArgumentException("소셜 로그인으로 이미 가입된 사용자입니다. 이메일을 사용할 수 없습니다.");
            }
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Member member = requestDto.toEntity().toBuilder().password(encodedPassword).build();
        memberRepository.save(member);
        return member.getId();
    }

    @Override
    public Long registerOrUpdateWithSocialLogin(SocialLoginRequestDto requestDto) {
        Optional<Member> existingMember = memberRepository.findByEmail(requestDto.getEmail());
        if (existingMember.isPresent()) {
            Member member = existingMember.get();
            if (!member.hasProvider(requestDto.getProvider())) {
                member.addProvider(requestDto.getProvider(), requestDto.getProviderId());
                return member.getId();
            }
        }
        Member member = requestDto.toEntity();
        return memberRepository.save(member).getId();
    }

    @Override
    public void updateMember(Long id, MemberUpdateRequestDto requestDto) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Member Id"));
        member.updateProfile(requestDto.getUsername(), requestDto.getEmail(),
            requestDto.getNickname(), passwordEncoder.encode(requestDto.getPassword()),
            requestDto.getProfileImageUrl());
    }

    @Override
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        memberRepository.delete(member);
    }

    @Override
    public MemberProfileResponseDto getMemberProfileByNickname(String nickname) {
        Member member = memberRepository.findByNickname(nickname)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return memberProfileResponseDtoFactory.createFromMember(member);
    }

    @Override
    public MyProfileResponseDto getMyProfile(String token) {
        Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return myProfileResponseDtoFactory.createFromMember(member);
    }
}

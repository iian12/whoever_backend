package com.jygoh.whoever.domain.member.service;

import com.jygoh.whoever.domain.member.dto.MemberCreateRequestDto;
import com.jygoh.whoever.domain.member.dto.MemberUpdateRequestDto;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.entity.Provider;
import com.jygoh.whoever.domain.member.entity.Role;
import com.jygoh.whoever.domain.member.profile.dto.MemberProfileResponseDto;
import com.jygoh.whoever.domain.member.profile.dto.MemberProfileResponseDtoFactory;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberProfileResponseDtoFactory memberProfileResponseDtoFactory;

    public MemberServiceImpl(MemberRepository memberRepository,
        BCryptPasswordEncoder passwordEncoder,
        MemberProfileResponseDtoFactory memberProfileResponseDtoFactory) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.memberProfileResponseDtoFactory = memberProfileResponseDtoFactory;
    }

    @Override
    public Long registerMember(MemberCreateRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
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

    private Member createNewMember(String email, String name, Provider provider,
        String profileImageUrl, String providerId) {
        // 새로운 Member 객체 생성
        Member newMember = Member.builder().email(email).nickname(name)  // nickname을 name으로 설정
            .profileImageUrl(profileImageUrl).providers(List.of(provider))
            .providerId(providerId) // providerId 설정
            .role(Role.MEMBER) // 기본 역할 설정
            .build();
        return memberRepository.save(newMember);
    }

    @Override
    public void updateMember(Long id, MemberUpdateRequestDto requestDto) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Member Id"));
        member.updateProfile(requestDto.getEmail(), requestDto.getNickname(),
            passwordEncoder.encode(requestDto.getPassword()), requestDto.getProfileImageUrl());
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
}

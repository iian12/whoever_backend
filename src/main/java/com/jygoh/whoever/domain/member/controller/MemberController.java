package com.jygoh.whoever.domain.member.controller;

import com.jygoh.whoever.domain.member.dto.MemberCreateRequestDto;
import com.jygoh.whoever.domain.member.profile.dto.MemberProfileResponseDto;
import com.jygoh.whoever.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody MemberCreateRequestDto requestDto) {
        Long memberId = memberService.registerMember(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }

    @GetMapping("/profile/{nickname}")
    public ResponseEntity<MemberProfileResponseDto> getMemberProfile(
        @PathVariable String nickname) {
        MemberProfileResponseDto profile = memberService.getMemberProfileByNickname(nickname);
        return ResponseEntity.ok(profile);
    }
}

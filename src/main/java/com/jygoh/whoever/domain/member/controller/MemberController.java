package com.jygoh.whoever.domain.member.controller;

import com.jygoh.whoever.domain.member.dto.MemberCreateRequestDto;
import com.jygoh.whoever.domain.member.dto.MyProfileResponseDto;
import com.jygoh.whoever.domain.member.service.MemberService;
import com.jygoh.whoever.global.security.jwt.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody MemberCreateRequestDto requestDto) {
        Long memberId = memberService.register(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }

    @GetMapping("/me")
    public ResponseEntity<MyProfileResponseDto> getMyProfile(HttpServletRequest request) {

        String token = TokenUtils.extractTokenFromRequest(request);

        MyProfileResponseDto responseDto = memberService.getMyProfile(token);

        return ResponseEntity.ok(responseDto);
    }

}

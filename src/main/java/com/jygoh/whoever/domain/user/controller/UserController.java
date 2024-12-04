package com.jygoh.whoever.domain.user.controller;

import com.jygoh.whoever.domain.user.dto.UserCreateReqDto;
import com.jygoh.whoever.domain.user.profile.dto.UserProfileResDto;
import com.jygoh.whoever.domain.user.profile.dto.NicknameRequestDto;
import com.jygoh.whoever.domain.user.service.UsersService;
import com.jygoh.whoever.global.auth.AuthenticationFacade;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
public class UserController {

    private final UsersService usersService;
    private final AuthenticationFacade authenticationFacade;

    public UserController(UsersService usersService, AuthenticationFacade authenticationFacade) {
        this.usersService = usersService;
        this.authenticationFacade = authenticationFacade;
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody UserCreateReqDto requestDto) {
        Long memberId = usersService.registerUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }

    @GetMapping("/profile/{nickname}")
    public ResponseEntity<UserProfileResDto> getMemberProfile(
        @PathVariable String nickname) {
        UserProfileResDto profile = usersService.getMemberProfileByNickname(nickname);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/set-nickname")
    public ResponseEntity<String> setNickname(@RequestBody @Valid NicknameRequestDto requestDto, HttpServletRequest request) {
        Long memberId = authenticationFacade.getCurrentMemberId();
        usersService.setNickname(memberId, requestDto.getNickname());
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }
}

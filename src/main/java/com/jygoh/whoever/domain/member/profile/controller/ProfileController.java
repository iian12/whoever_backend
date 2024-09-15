package com.jygoh.whoever.domain.member.profile.controller;

import com.jygoh.whoever.domain.member.profile.dto.MyBasicInfoResponseDto;
import com.jygoh.whoever.domain.member.profile.dto.MyCommentsResponseDto;
import com.jygoh.whoever.domain.member.profile.dto.MyLikedPostsResponseDto;
import com.jygoh.whoever.domain.member.profile.dto.MyPostsResponseDto;
import com.jygoh.whoever.domain.member.profile.service.ProfileService;
import com.jygoh.whoever.global.security.jwt.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    public ResponseEntity<MyBasicInfoResponseDto> getMyBasicInfo(HttpServletRequest request) {
        String token = TokenUtils.extractTokenFromRequest(request);
        MyBasicInfoResponseDto basicInfo = profileService.getMyProfile(token);
        return ResponseEntity.ok(basicInfo);
    }

    @GetMapping("/me/posts")
    public ResponseEntity<MyPostsResponseDto> getMyPosts(HttpServletRequest request) {
        String token = TokenUtils.extractTokenFromRequest(request);
        MyPostsResponseDto myPostsResponseDto = profileService.getMyPosts(token);
        return ResponseEntity.ok(myPostsResponseDto);
    }

    @GetMapping("/me/comments")
    public ResponseEntity<MyCommentsResponseDto> getMyComments(HttpServletRequest request) {
        String token = TokenUtils.extractTokenFromRequest(request);
        MyCommentsResponseDto myCommentsResponseDto = profileService.getMyComments(token);
        return ResponseEntity.ok(myCommentsResponseDto);
    }

    @GetMapping("/me/liked-posts")
    public ResponseEntity<MyLikedPostsResponseDto> getMyLikedPosts(HttpServletRequest request) {
        String token = TokenUtils.extractTokenFromRequest(request);
        MyLikedPostsResponseDto myLikedPostsResponseDto = profileService.getMyLikedPosts(token);
        return ResponseEntity.ok(myLikedPostsResponseDto);
    }
}

package com.jygoh.whoever.domain.user.profile.service;

import com.jygoh.whoever.domain.comment.repository.CommentRepository;
import com.jygoh.whoever.domain.follow.dto.FollowResponseDto;
import com.jygoh.whoever.domain.follow.repository.FollowRepository;
import com.jygoh.whoever.domain.user.entity.Users;
import com.jygoh.whoever.domain.user.profile.dto.MyBasicInfoResponseDto;
import com.jygoh.whoever.domain.user.profile.dto.MyCommentsResponseDto;
import com.jygoh.whoever.domain.user.profile.dto.MyLikedPostsResponseDto;
import com.jygoh.whoever.domain.user.profile.dto.MyPostsResponseDto;
import com.jygoh.whoever.domain.user.repository.UserRepository;
import com.jygoh.whoever.domain.post.like.PostLikeRepository;
import com.jygoh.whoever.domain.post.model.Post;
import com.jygoh.whoever.domain.post.repository.PostRepository;
import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public ProfileServiceImpl(UserRepository userRepository, FollowRepository followRepository,
        PostRepository postRepository, PostLikeRepository postLikeRepository,
        CommentRepository commentRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.commentRepository = commentRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public MyBasicInfoResponseDto getMyProfile(String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        // 사용자 기본 정보를 조회
        Users users = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return MyBasicInfoResponseDto.builder().email(users.getEmail())
            .nickname(users.getNickname()).profileImageUrl(users.getProfileImageUrl())
            .following(getFollowingList(userId)).followerCount(users.getFollowerCount()).build();
    }

    private List<FollowResponseDto> getFollowingList(Long userId) {
        return followRepository.findById_FollowerId(userId).stream().map(follow -> {
            Users followee = userRepository.findById(follow.getFolloweeId())
                .orElseThrow(() -> new IllegalArgumentException("Followee not found"));
            return new FollowResponseDto(followee.getId(), followee.getNickname());
        }).collect(Collectors.toList());
    }

    @Override
    public MyPostsResponseDto getMyPosts(String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        List<MyPostsResponseDto.PostForProfileDto> posts = postRepository.findAllByAuthorId(
            userId).stream().map(
            post -> new MyPostsResponseDto.PostForProfileDto(post.getId(), post.getTitle(),
                post.getContent())).collect(Collectors.toList());
        return MyPostsResponseDto.builder().posts(posts).build();
    }

    @Override
    public MyCommentsResponseDto getMyComments(String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        List<MyCommentsResponseDto.CommentForProfileDto> comments = commentRepository.findByAuthorId(
                userId).stream().map(
                comment -> new MyCommentsResponseDto.CommentForProfileDto(comment.getId(),
                    comment.getContent(),
                    new MyCommentsResponseDto.CommentForProfileDto.PostForCommentDto(
                        comment.getPostId(), ""))) // Assuming post title is not needed
            .collect(Collectors.toList());
        return MyCommentsResponseDto.builder().comments(comments).build();
    }

    @Override
    public MyLikedPostsResponseDto getMyLikedPosts(String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        List<MyLikedPostsResponseDto.PostForProfileDto> likedPosts = postLikeRepository.findByUserId(
            userId).stream().map(like -> {
            Post post = postRepository.findById(like.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
            return new MyLikedPostsResponseDto.PostForProfileDto(post.getId(), post.getTitle(),
                post.getContent());
        }).collect(Collectors.toList());
        return MyLikedPostsResponseDto.builder().likedPosts(likedPosts).build();
    }
}

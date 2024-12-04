package com.jygoh.whoever.domain.user.profile.dto;

import com.jygoh.whoever.domain.user.entity.Users;
import com.jygoh.whoever.domain.post.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserProfileResDtoFactory {

    private final PostRepository postRepository;

    public UserProfileResDtoFactory(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public UserProfileResDto createFromMember(Users users) {
        // 작성한 글의 제목을 조회
        List<UserProfileResDto.PostForProfileDto> posts = postRepository.findAllByAuthorId(
                users.getId()).stream().map(
                post -> new UserProfileResDto.PostForProfileDto(post.getId(), post.getTitle()))
            .collect(Collectors.toList());
        int followerCount = users.getFollowerCount();
        return UserProfileResDto.builder().nickname(users.getNickname()).posts(posts)
            .followerCount(followerCount).build();
    }
}

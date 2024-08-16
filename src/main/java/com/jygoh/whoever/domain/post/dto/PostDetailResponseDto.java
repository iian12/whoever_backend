package com.jygoh.whoever.domain.post.dto;

import com.jygoh.whoever.domain.comment.dto.CommentDto;
import com.jygoh.whoever.domain.hashtag.dto.HashtagDto;
import com.jygoh.whoever.domain.post.model.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PostDetailResponseDto {

    private Long id;
    private String title;
    private String content;
    private String authorNickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentDto> comments;
    private List<HashtagDto> hashtags;

    @Builder
    public PostDetailResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorNickname = post.getAuthorNickname();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.comments = post.getComments() != null ?
                post.getComments().stream().map(CommentDto::new).collect(Collectors.toList()) :
                new ArrayList<>();
        this.hashtags = post.getHashtags().stream()
            .map(HashtagDto::new)
            .collect(Collectors.toList());
    }
}

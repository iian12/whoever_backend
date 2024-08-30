package com.jygoh.whoever.domain.post.dto;

import com.jygoh.whoever.domain.post.model.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostListResponseDto {

    private Long id;
    private String title;
    private String authorNickname;
    private String thumbnailUrl;
    private LocalDateTime createdAt;

    @Builder
    public PostListResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.authorNickname = post.getAuthorNickname();
        this.thumbnailUrl = post.getThumbnailUrl();
        this.createdAt = post.getCreatedAt();
    }
}

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
    private int likeCount;
    private int commentCount;
    private int viewCount;
    private LocalDateTime createdAt;

    @Builder
    public PostListResponseDto(Post post, String authorNickname) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.authorNickname = authorNickname;
        this.thumbnailUrl = post.getThumbnailUrl();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.viewCount = post.getViewCount();
        this.createdAt = post.getCreatedAt();
    }
}

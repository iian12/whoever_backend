package com.jygoh.whoever.domain.post.dto;

import com.jygoh.whoever.domain.post.model.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostCreateRequestDto {

    private String title;
    private String content;
    private List<String> hashtagNames;

    @Builder
    public PostCreateRequestDto(String title, String content, List<String> hashtagNames) {
        this.title = title;
        this.content = content;
        this.hashtagNames = hashtagNames;
    }

    public Post toEntity(Long authorId, String authorNickname, String thumbnailUrl, List<Long> hashtagIds) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .authorId(authorId)
                .authorNickname(authorNickname)
                .thumbnailUrl(thumbnailUrl)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .hashtagIds(hashtagIds)
                .build();
    }
}
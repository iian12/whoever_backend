package com.jygoh.whoever.domain.post.dto;

import com.jygoh.whoever.domain.post.model.Post;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequestDto {

    private String title;
    private String content;
    private List<String> hashtagNames;
    private Long categoryId;

    @Builder
    public PostCreateRequestDto(String title, String content, List<String> hashtagNames,
        Long categoryId) {
        this.title = title;
        this.content = content;
        this.hashtagNames = hashtagNames;
        this.categoryId = categoryId;
    }

    public Post toEntity(Long authorId, String thumbnailUrl, List<Long> hashtagIds,
        Long categoryId) {
        return Post.builder().title(this.title).content(this.content).authorId(authorId)
            .thumbnailUrl(thumbnailUrl).createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now()).hashtagIds(hashtagIds).categoryId(categoryId).build();
    }
}
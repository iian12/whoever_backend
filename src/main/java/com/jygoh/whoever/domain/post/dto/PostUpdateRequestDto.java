package com.jygoh.whoever.domain.post.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDto {

    private String title;

    private String content;

    private List<String> hashtagNames;

    private Long categoryId;

    @Builder
    public PostUpdateRequestDto(String title, String content, List<String> hashtagNames,
        Long categoryId) {
        this.title = title;
        this.content = content;
        this.hashtagNames = hashtagNames;
        this.categoryId = categoryId;
    }
}

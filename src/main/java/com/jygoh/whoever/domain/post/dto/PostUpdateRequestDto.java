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

    private List<Long> hashtagIds;

    @Builder
    public PostUpdateRequestDto(String title, String content, List<Long> hashtagIds) {
        this.title = title;
        this.content = content;
        this.hashtagIds = hashtagIds;
    }
}

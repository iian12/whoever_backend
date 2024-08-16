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

    @Builder
    public PostUpdateRequestDto(String title, String content, List<String> hashtagNames) {
        this.title = title;
        this.content = content;
        this.hashtagNames = hashtagNames;
    }
}

package com.jygoh.whoever.domain.post.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfilePostResponseDto {

    private Long id;
    private String title;
    private String content;
    private String authorNickname;
    private String thumbnailUrl;
    private LocalDateTime createdAt;

    @Builder
    public ProfilePostResponseDto(Long id, String title, String content, String authorNickname,
        String thumbnailUrl, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorNickname = authorNickname;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = createdAt;
    }
}
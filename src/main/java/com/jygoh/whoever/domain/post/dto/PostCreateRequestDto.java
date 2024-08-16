package com.jygoh.whoever.domain.post.dto;

import com.jygoh.whoever.domain.hashtag.model.Hashtag;
import com.jygoh.whoever.domain.member.entity.Member;
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

    @Builder
    public PostCreateRequestDto(String title, String content, List<String> hashtagNames) {
        this.title = title;
        this.content = content;
        this.hashtagNames = hashtagNames;
    }

    public Post toEntity(Member author, String authorNickname, List<Hashtag> hashtags) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .author(author)
                .authorNickname(authorNickname)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .hashtags(hashtags)
                .build();
    }
}
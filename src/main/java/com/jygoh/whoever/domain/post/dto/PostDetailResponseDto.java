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
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

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
        this.content = convertMarkdownToHtml(post.getContent());
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

    private String convertMarkdownToHtml(String markdownContent) {
        Parser parser = Parser.builder().build();

        HtmlRenderer renderer = HtmlRenderer.builder().build();

        return renderer.render(parser.parse(markdownContent));
    }
}

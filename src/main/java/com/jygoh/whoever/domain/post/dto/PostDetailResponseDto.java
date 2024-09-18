package com.jygoh.whoever.domain.post.dto;

import com.jygoh.whoever.domain.comment.dto.CommentDto;
import com.jygoh.whoever.domain.hashtag.dto.HashtagDto;
import com.jygoh.whoever.domain.post.model.Post;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private Set<CommentDto> comments;
    private Set<HashtagDto> hashtags;
    private int viewCount;
    private int commentCount;

    @Builder
    public PostDetailResponseDto(Long id, String title, String content, String authorNickname,
        LocalDateTime createdAt, LocalDateTime updatedAt, Set<CommentDto> comments,
        Set<HashtagDto> hashtags, int viewCount, int commentCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorNickname = authorNickname;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.comments = comments;
        this.hashtags = hashtags;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
    }

    public PostDetailResponseDto(Post post, Set<CommentDto> commentDtos,
        Set<HashtagDto> hashtagDtos, String authorNickname) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = convertMarkdownToHtml(post.getContent());
        this.authorNickname = authorNickname;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.comments = commentDtos != null ? commentDtos : new HashSet<>();
        this.hashtags = hashtagDtos != null ? hashtagDtos : new HashSet<>();
        this.viewCount = post.getViewCount();
        this.commentCount = post.getCommentCount();
    }

    private String convertMarkdownToHtml(String markdownContent) {
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(parser.parse(markdownContent));
    }
}

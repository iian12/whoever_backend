package com.jygoh.whoever.domain.post.model;

import com.jygoh.whoever.domain.comment.model.Comment;
import com.jygoh.whoever.domain.hashtag.model.Hashtag;
import com.jygoh.whoever.domain.member.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(
        name = "PostHashtag",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private List<Hashtag> hashtags;

    @Builder
    public Post(String title, String content, Member author, LocalDateTime createdAt, LocalDateTime updatedAt, List<Comment> comments, List<Hashtag> hashtags) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.comments = comments;
        this.hashtags = hashtags;
    }

    public void update(String title, String content, List<Hashtag> hashtags) {
        this.title = title;
        this.content = content;
        this.hashtags = hashtags;
        this.updatedAt = LocalDateTime.now(); // 수정 시간 업데이트
    }
}

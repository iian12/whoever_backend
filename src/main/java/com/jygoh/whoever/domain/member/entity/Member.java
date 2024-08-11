package com.jygoh.whoever.domain.member.entity;

import com.jygoh.whoever.domain.comment.model.Comment;
import com.jygoh.whoever.domain.post.model.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "author")
    private List<Post> posts;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments;

    @Builder
    public Member(String username, String password, String email, Role role, List<Post> posts, List<Comment> comments) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.posts = posts;
        this.comments = comments;
    }
}

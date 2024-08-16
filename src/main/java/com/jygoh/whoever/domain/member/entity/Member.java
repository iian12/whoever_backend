package com.jygoh.whoever.domain.member.entity;

import com.jygoh.whoever.domain.comment.model.Comment;
import com.jygoh.whoever.domain.post.model.Post;
import jakarta.persistence.*;

import java.util.ArrayList;
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

    private String nickname;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments = new ArrayList<>();

    @Builder(toBuilder = true)
    public Member(String username, String password, String email, String nickname, List<Post> posts, Role role, List<Comment> comments) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.posts = posts != null ? posts : new ArrayList<>();
        this.role = role;
        this.comments = comments != null ? comments : new ArrayList<>();
    }

    public void updateProfile(String username, String email, String nickname, String encodedPassword) {
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.password = encodedPassword;
    }
}
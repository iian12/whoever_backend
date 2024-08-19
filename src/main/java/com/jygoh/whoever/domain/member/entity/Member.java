package com.jygoh.whoever.domain.member.entity;

import com.jygoh.whoever.domain.comment.model.Comment;
import com.jygoh.whoever.domain.follow.Follow;
import com.jygoh.whoever.domain.friend.model.Friendship;
import com.jygoh.whoever.domain.post.model.Post;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToMany(mappedBy = "member1", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Friendship> friendshipsAsMember1 = new HashSet<>();

    @OneToMany(mappedBy = "member2", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Friendship> friendshipsAsMember2 = new HashSet<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> following = new HashSet<>();

    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> followers = new HashSet<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments = new ArrayList<>();

    @Builder(toBuilder = true)
    public Member(String username, String password, String email, String nickname,
        Set<Friendship> friendshipsAsMember1, Set<Friendship> friendshipsAsMember2,
        Set<Follow> following, Set<Follow> followers,
        List<Post> posts, Role role, List<Comment> comments) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.friendshipsAsMember1 = friendshipsAsMember1 != null ? friendshipsAsMember1 : new HashSet<>();
        this.friendshipsAsMember2 = friendshipsAsMember2 != null ? friendshipsAsMember2 : new HashSet<>();
        this.following = following != null ? following : new HashSet<>();
        this.followers = followers != null ? followers : new HashSet<>();
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
package com.jygoh.whoever.domain.member.entity;

import com.jygoh.whoever.domain.comment.model.Comment;
import com.jygoh.whoever.domain.follow.Follow;
import com.jygoh.whoever.domain.post.model.Post;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    private int followerCount;

    @Builder(toBuilder = true)
    public Member(String username, String password, String email, String nickname,
        Set<Follow> following, Set<Follow> followers,
        List<Post> posts, Role role, List<Comment> comments, int followerCount) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.following = following != null ? following : new HashSet<>();
        this.followers = followers != null ? followers : new HashSet<>();
        this.posts = posts != null ? posts : new ArrayList<>();
        this.role = role;
        this.comments = comments != null ? comments : new ArrayList<>();
        this.followerCount = followerCount;
    }

    public void updateProfile(String username, String email,
                                String nickname, String encodedPassword) {
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.password = encodedPassword;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void increaseFollowerCount() {
        this.followerCount++;
    }

    public void decreaseFollowerCount() {
        if (this.followerCount > 0) {
            this.followerCount--;
        }
    }
}
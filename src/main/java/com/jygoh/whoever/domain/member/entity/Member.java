package com.jygoh.whoever.domain.member.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

    private String profileImageUrl;

    @ElementCollection
    @CollectionTable(name = "MemberFollowing", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "following_id")
    private Set<Long> followingIds = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "MemberFollowers", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "follower_id")
    private Set<Long> followerIds = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "MemberPosts", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "post_id")
    private List<Long> postIds = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    @ElementCollection
    @CollectionTable(name = "MemberComments", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "comment_id")
    private List<Long> commentIds = new ArrayList<>();

    private int followerCount;

    @Builder(toBuilder = true)
    public Member(String username, String password, String email, String nickname,
        String profileImageUrl, Set<Long> followingIds, Set<Long> followerIds, List<Long> postIds,
        Role role, List<Long> commentIds, int followerCount) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.followingIds = followingIds != null ? followingIds : new HashSet<>();
        this.followerIds = followerIds != null ? followerIds : new HashSet<>();
        this.postIds = postIds != null ? postIds : new ArrayList<>();
        this.role = role;
        this.commentIds = commentIds != null ? commentIds : new ArrayList<>();
        this.followerCount = followerCount;
    }

    public void updateProfile(String username, String email, String nickname,
        String encodedPassword, String profileImageUrl) {
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.password = encodedPassword;
        this.profileImageUrl = profileImageUrl;
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
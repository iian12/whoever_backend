package com.jygoh.whoever.domain.member.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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

    private String email;

    private String password;

    private String nickname;

    private String profileImageUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_providers", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private Set<Provider> providers = new HashSet<>();

    private String providerId;


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
    public Member(String password, String email, String nickname,
        Set<Provider> providers, String providerId, String profileImageUrl,
        Set<Long> followingIds, Set<Long> followerIds, List<Long> postIds, Role role,
        List<Long> commentIds, int followerCount) {
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.providers = providers != null ? new HashSet<>(providers) : new HashSet<>();
        this.providerId = providerId;
        this.profileImageUrl = profileImageUrl;
        this.followingIds = followingIds != null ? followingIds : new HashSet<>();
        this.followerIds = followerIds != null ? followerIds : new HashSet<>();
        this.postIds = postIds != null ? postIds : new ArrayList<>();
        this.role = role;
        this.commentIds = commentIds != null ? commentIds : new ArrayList<>();
        this.followerCount = followerCount;
    }

    public void updateProfile(String email, String nickname,
        String encodedPassword, String profileImageUrl) {
        this.email = email;
        this.nickname = nickname;
        this.password = encodedPassword;
        this.profileImageUrl = profileImageUrl;
    }

    public void addProvider(Provider provider, String providerId) {
        this.providers.add(provider);
        if (providerId != null && !providerId.isEmpty()) {
            this.providerId = providerId;
        }
    }

    public void updateProfileImageUrl(String profileImageUrl) {
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
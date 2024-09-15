package com.jygoh.whoever.domain.category;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long memberId;

    @ElementCollection
    private List<Long> postIds = new ArrayList<>();

    @Builder
    public Category(String name, Long memberId, List<Long> postIds) {
        this.name = name;
        this.memberId = memberId;
        this.postIds = postIds;
    }

    public void addPost(Long postId) {
        this.postIds.add(postId);
    }

    public int getPostCount() {
        return postIds.size();
    }
}

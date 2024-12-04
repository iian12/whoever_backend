package com.jygoh.whoever.domain.category;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private Long userId;

    private int postCount;

    @Builder
    public Category(String name, Long userId, int postCount) {
        this.name = name;
        this.userId = userId;
        this.postCount = postCount;
    }

    public void incrementPostCount() {
        this.postCount++;
    }

    public void decrementPostCount() {
        this.postCount--;
    }

    public void updateName(String newName) {
        this.name = newName;
    }
}

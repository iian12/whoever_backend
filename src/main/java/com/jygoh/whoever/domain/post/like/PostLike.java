package com.jygoh.whoever.domain.post.like;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@IdClass(LikeId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

    @Id
    private Long postId;

    @Id
    private Long userId;

    @Builder
    public PostLike(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostLike postLike = (PostLike) o;
        return postId.equals(postLike.postId) && userId.equals(postLike.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId);
    }

}

package com.jygoh.whoever.domain.post.view.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class ViewId implements Serializable {

    private Long memberId;

    private Long postId;

    public ViewId(Long memberId, Long postId) {
        this.memberId = memberId;
        this.postId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ViewId viewId = (ViewId) o;
        return Objects.equals(memberId, viewId.memberId) && Objects.equals(postId, viewId.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, postId);
    }
}

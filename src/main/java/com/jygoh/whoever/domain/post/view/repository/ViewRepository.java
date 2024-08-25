package com.jygoh.whoever.domain.post.view.repository;

import com.jygoh.whoever.domain.post.view.model.View;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewRepository extends JpaRepository<View, Long> {

    boolean existsByPostIdAndMemberId(Long postId, Long memberId);

    View findByMemberIdAndPostId(Long memberId, Long postId);
}

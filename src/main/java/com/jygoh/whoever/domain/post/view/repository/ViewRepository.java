package com.jygoh.whoever.domain.post.view.repository;

import com.jygoh.whoever.domain.post.view.model.View;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewRepository extends JpaRepository<View, Long> {

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    Optional<View> findByMemberIdAndPostId(Long memberId, Long postId);
}

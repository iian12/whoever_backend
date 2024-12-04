package com.jygoh.whoever.domain.post.view.repository;

import com.jygoh.whoever.domain.post.view.model.View;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ViewRepository extends JpaRepository<View, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<View> findByUserIdAndPostId(Long memberId, Long postId);

    @Query("SELECt v FROM View v WHERE v.userId = :userId ORDER BY v.updatedAt DESC")
    List<View> findTop10ByUserIdOrderByUpdatedAtDesc(@Param("userId") Long userId);
}

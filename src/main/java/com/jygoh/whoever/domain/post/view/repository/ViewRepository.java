package com.jygoh.whoever.domain.post.view.repository;

import com.jygoh.whoever.domain.post.view.model.View;
import com.jygoh.whoever.domain.post.view.model.ViewId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewRepository extends JpaRepository<View, ViewId> {

    Optional<View> findById(ViewId viewId);

    boolean existsById(ViewId viewId);
}

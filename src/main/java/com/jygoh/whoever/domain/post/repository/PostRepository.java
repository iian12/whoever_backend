package com.jygoh.whoever.domain.post.repository;

import com.jygoh.whoever.domain.post.model.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByAuthorId(Long authorId);

    List<Post> findByTitleContaining(String keyword);

    List<Post> findByAuthorIdAndTitleContaining(Long authorId, String keyword);

    List<Post> findByIdIn(List<Long> postIds);
}

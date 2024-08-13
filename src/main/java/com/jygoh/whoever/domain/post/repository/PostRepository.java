package com.jygoh.whoever.domain.post.repository;

import com.jygoh.whoever.domain.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}

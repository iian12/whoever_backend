package com.jygoh.whoever.domain.comment.repository;

import com.jygoh.whoever.domain.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

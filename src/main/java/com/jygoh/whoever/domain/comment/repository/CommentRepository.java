package com.jygoh.whoever.domain.comment.repository;

import com.jygoh.whoever.domain.comment.model.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(Long postId);

    List<Comment> findByAuthorId(Long authorId);
}

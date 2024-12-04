package com.jygoh.whoever.domain.comment.dto;

import com.jygoh.whoever.domain.comment.model.Comment;
import com.jygoh.whoever.domain.user.entity.Users;
import com.jygoh.whoever.domain.user.repository.UserRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentDto {

    private Long id;
    private String content;
    private String authorNickname;

    public CommentDto(Comment comment, UserRepository userRepository) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.authorNickname = findAuthorNickname(comment.getAuthorId(), userRepository);
    }

    private String findAuthorNickname(Long authorId, UserRepository userRepository) {
        return userRepository.findById(authorId).map(Users::getNickname)
            .orElse("Unknown"); // Default value if the author is not found
    }
}

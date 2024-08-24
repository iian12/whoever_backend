package com.jygoh.whoever.domain.comment.dto;

import com.jygoh.whoever.domain.comment.model.Comment;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentDto {

    private Long id;
    private String content;
    private String authorNickname;

    public CommentDto(Comment comment, MemberRepository memberRepository) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.authorNickname = findAuthorNickname(comment.getAuthorId(), memberRepository);
    }

    private String findAuthorNickname(Long authorId, MemberRepository memberRepository) {
        return memberRepository.findById(authorId)
                .map(Member::getNickname)
                .orElse("Unknown"); // Default value if the author is not found
    }
}

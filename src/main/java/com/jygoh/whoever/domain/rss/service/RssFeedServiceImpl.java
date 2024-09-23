package com.jygoh.whoever.domain.rss.service;

import com.jygoh.whoever.domain.member.repository.MemberRepository;
import com.jygoh.whoever.domain.post.model.Post;
import com.jygoh.whoever.domain.post.repository.PostRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RssFeedServiceImpl implements RssFeedService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public RssFeedServiceImpl(PostRepository postRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public List<Post> getPostByMemberNickname(String nickname) {
        Long memberId = memberRepository.findByNickname(nickname)
            .orElseThrow(() -> new IllegalArgumentException("Member not Found")).getId();
        return postRepository.findAllByAuthorId(memberId);
    }
}

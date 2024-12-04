package com.jygoh.whoever.domain.rss.service;

import com.jygoh.whoever.domain.user.repository.UserRepository;
import com.jygoh.whoever.domain.post.model.Post;
import com.jygoh.whoever.domain.post.repository.PostRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RssFeedServiceImpl implements RssFeedService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public RssFeedServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Post> getPostByMemberNickname(String nickname) {
        Long userId = userRepository.findByNickname(nickname)
            .orElseThrow(() -> new IllegalArgumentException("Member not Found")).getId();
        return postRepository.findAllByAuthorId(userId);
    }
}

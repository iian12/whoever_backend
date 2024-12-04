package com.jygoh.whoever.domain.rss.service;

import com.jygoh.whoever.domain.post.model.Post;
import java.util.List;

public interface RssFeedService {

    List<Post> getPostByMemberNickname(String nickname);


}

package com.jygoh.whoever.domain.hashtag.service;

import com.jygoh.whoever.domain.hashtag.model.Hashtag;
import java.util.List;
import java.util.Set;

public interface HashtagService {

    Hashtag createOrGetHashtag(String name);

    Set<Hashtag> findOrCreateHashtags(List<String> names);
}

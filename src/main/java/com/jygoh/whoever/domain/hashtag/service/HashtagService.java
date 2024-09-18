package com.jygoh.whoever.domain.hashtag.service;

import com.jygoh.whoever.domain.hashtag.model.Hashtag;
import java.util.List;

public interface HashtagService {

    Hashtag createOrGetHashtag(String name);

    List<Hashtag> findOrCreateHashtags(List<String> names);
}

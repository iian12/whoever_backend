package com.jygoh.whoever.domain.hashtag.service;

import com.jygoh.whoever.domain.hashtag.model.Hashtag;
import com.jygoh.whoever.domain.hashtag.repository.HashtagRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    public HashtagServiceImpl(HashtagRepository hashtagRepository) {
        this.hashtagRepository = hashtagRepository;
    }

    @Override
    public Hashtag createOrGetHashtag(String name) {
        return hashtagRepository.findByName(name)
            .orElseGet(() -> hashtagRepository.save(Hashtag.builder().name(name).build()));
    }

    @Override
    public List<Hashtag> findOrCreateHashtags(List<String> names) {
        return names.stream()
            .map(this::createOrGetHashtag)
            .collect(Collectors.toList());
    }
}

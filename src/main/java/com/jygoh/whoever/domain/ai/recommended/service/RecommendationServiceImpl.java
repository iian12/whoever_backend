package com.jygoh.whoever.domain.ai.recommended.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RecommendationServiceImpl implements RecommendationService {

    @Override
    public String getRecommendedPosts(String memberId) {
        return "";
    }

    @Override
    public String getRecommendedHashtags(String content) {
        return "";
    }
}

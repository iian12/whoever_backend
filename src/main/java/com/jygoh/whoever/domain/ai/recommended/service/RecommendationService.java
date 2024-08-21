package com.jygoh.whoever.domain.ai.recommended.service;

public interface RecommendationService {

    String getRecommendedPosts(String userId);

    String getRecommendedHashtags(String content);
}

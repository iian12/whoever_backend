package com.jygoh.whoever.domain.ai.service;

public interface RecommendationService {

    String getRecommendedPosts(String userId);

    String getRecommendedHashtags(String content);
}

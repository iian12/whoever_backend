package com.jygoh.whoever.domain.ai.recommended.service;

public interface RecommendationService {

    String getRecommendedPosts(String memberId);

    String getRecommendedHashtags(String content);
}

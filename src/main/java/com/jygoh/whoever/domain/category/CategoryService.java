package com.jygoh.whoever.domain.category;

import java.util.List;

public interface CategoryService {

    List<CategoryResponseDto> getCategoriesForMember(String token);

    Long createCategory(String token, String categoryName);

    Long createOrUpdateDefaultCategory(Long memberId);

}

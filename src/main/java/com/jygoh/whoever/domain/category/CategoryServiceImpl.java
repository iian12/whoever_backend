package com.jygoh.whoever.domain.category;

import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public CategoryServiceImpl(CategoryRepository categoryRepository, JwtTokenProvider jwtTokenProvider) {
        this.categoryRepository = categoryRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public List<CategoryResponseDto> getCategoriesForMember(String token) {
        Long memberId = jwtTokenProvider.getUserIdFromToken(token);
        List<Category> categories = categoryRepository.findByMemberId(memberId);
        return categories.stream()
            .map(CategoryResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public Long createOrUpdateDefaultCategory(Long userId) {
        Category defaultCategory = categoryRepository.findByMemberIdAndName(userId, "없음")
            .orElseGet(() -> {
                Category newCategory = Category.builder()
                    .name("없음")
                    .userId(userId)
                    .postCount(0)
                    .build();
                return categoryRepository.save(newCategory);
            });

        return defaultCategory.getId();
    }

    @Override
    public Long createCategory(String token, String categoryName) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        // 카테고리 이름이 유효한지 확인
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty.");
        }

        // 같은 이름의 카테고리가 이미 존재하는지 확인
        Category existingCategory = categoryRepository.findByMemberIdAndName(userId, categoryName)
            .orElse(null);
        if (existingCategory != null) {
            return existingCategory.getId();
        }

        // 새 카테고리 생성
        Category newCategory = Category.builder()
            .name(categoryName)
            .userId(userId)
            .postCount(0)
            .build();

        Category savedCategory = categoryRepository.save(newCategory);
        return savedCategory.getId();
    }
}

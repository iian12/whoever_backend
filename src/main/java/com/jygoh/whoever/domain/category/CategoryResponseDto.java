package com.jygoh.whoever.domain.category;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryResponseDto {

    private Long id;
    private String name;

    @Builder
    public CategoryResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryResponseDto fromEntity(Category category) {
        return CategoryResponseDto.builder()
            .id(category.getId())
            .name(category.getName())
            .build();
    }
}

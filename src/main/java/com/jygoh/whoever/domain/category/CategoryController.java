package com.jygoh.whoever.domain.category;

import com.jygoh.whoever.global.security.jwt.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getCategoriesForMember(
        HttpServletRequest request) {
        String token = TokenUtils.extractTokenFromRequest(request);
        List<CategoryResponseDto> categories = categoryService.getCategoriesForMember(token);

        return ResponseEntity.ok(categories);
    }

}

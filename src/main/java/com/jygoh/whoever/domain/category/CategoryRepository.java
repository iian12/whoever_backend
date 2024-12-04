package com.jygoh.whoever.domain.category;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserId(Long userId);
    Optional<Category> findByUserIdAndName(Long userId, String name);
    Optional<Category> findByIdAndUserId(Long id, Long userId);

}

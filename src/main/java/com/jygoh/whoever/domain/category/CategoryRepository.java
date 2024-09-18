package com.jygoh.whoever.domain.category;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByMemberId(Long memberId);
    Optional<Category> findByMemberIdAndName(Long memberId, String name);
    Optional<Category> findByIdAndMemberId(Long id, Long memberId);

}

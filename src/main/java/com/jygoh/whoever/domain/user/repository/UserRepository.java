package com.jygoh.whoever.domain.user.repository;

import com.jygoh.whoever.domain.user.entity.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}

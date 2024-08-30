package com.jygoh.whoever.domain.member.repository;

import com.jygoh.whoever.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUsernameAndEmail(String username, String email);

    Optional<Member> findByNickname(String nickname);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

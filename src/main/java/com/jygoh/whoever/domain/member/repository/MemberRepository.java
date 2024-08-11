package com.jygoh.whoever.domain.member.repository;

import com.jygoh.whoever.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}

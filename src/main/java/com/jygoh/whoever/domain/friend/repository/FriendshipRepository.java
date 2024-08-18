package com.jygoh.whoever.domain.friend.repository;

import com.jygoh.whoever.domain.friend.model.Friendship;
import com.jygoh.whoever.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    boolean existsByMember1AndMember2(Member member1, Member member2);

    boolean existsByMember2AndMember1(Member member1, Member member2);

    // 특정 사용자의 모든 친구 관계를 조회하는 메서드
    List<Friendship> findAllByMember1OrMember2(Member member1, Member member2);

    // 친구 관계를 삭제하는 메서드 (Optional로 반환하여 처리 가능)
    void deleteByMember1AndMember2(Member member1, Member member2);

    void deleteByMember2AndMember1(Member member1, Member member2);
}

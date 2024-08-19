package com.jygoh.whoever.domain.follow;

import com.jygoh.whoever.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerAndFollowee(Member follower, Member followee);

    // 특정 사용자의 팔로워 목록 조회
    List<Follow> findAllByFollowee(Member followee);

    // 특정 사용자의 팔로잉 목록 조회
    List<Follow> findAllByFollower(Member follower);
}

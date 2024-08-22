package com.jygoh.whoever.domain.member.otp;

import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.otp.model.OTP;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPRepository extends JpaRepository<OTP, Long> {

    Optional<OTP> findByMemberAndOtp(Member member, String otp);

}

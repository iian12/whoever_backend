package com.jygoh.whoever.domain.member.otp.service;

import com.jygoh.whoever.domain.member.otp.OTPRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OtpServiceImpl implements OtpService {

    private final OTPRepository otpRepository;

    public OtpServiceImpl(OTPRepository otpRepository) {
        this.otpRepository = otpRepository;
    }


    @Override
    @Scheduled(fixedRate = 600000)
    public void deleteExpiredOtps() {
        otpRepository.deleteExpiredOtps();
    }
}

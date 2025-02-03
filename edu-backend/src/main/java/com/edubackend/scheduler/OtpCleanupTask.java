package com.edubackend.scheduler;



import com.edubackend.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OtpCleanupTask {

    @Autowired
    private OtpRepository otpRepository;

    @Scheduled(cron = "0 0 * * * ?") // Runs every hour
    public void deleteExpiredOtps() {
        System.out.println("runnign scheduler");
        otpRepository.deleteAll(otpRepository.findAll().stream()
                .filter(otp -> otp.getExpirationTime().isBefore(LocalDateTime.now()))
                .toList());
    }
}

package com.da.Attendance.service.Imp;

import com.da.Attendance.model.Otp;
import com.da.Attendance.repository.OtpRepository;
import com.da.Attendance.service.MailService;
import com.da.Attendance.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpServiceImp implements OtpService {
    @Autowired
    private MailService mailService;

    @Autowired
    private OtpRepository otpRepository;

    @Override
    public void generateAndSendOtp(String userEmail, String userId) {
        String otpCode = String.valueOf(new Random().nextInt(900000) + 100000);
        Instant expiresAt = Instant.now().plus(5, ChronoUnit.MINUTES);

        Otp otp = new Otp();
        otp.setOtp(otpCode);
        otp.setExpiration(expiresAt);
        otp.setUserId(userId);

        otpRepository.save(otp);
        mailService.sendOtp(userEmail, otpCode);
    }

    @Override
    public boolean verifyOtp(String userId, String inputOtp) {
        Optional<Otp> otpOpt = otpRepository.findTopByUserIdOrderByExpirationDesc(userId);
        if (otpOpt.isEmpty()) return false;

        Otp latestOtp = otpOpt.get();
        return Instant.now().isBefore(latestOtp.getExpiration()) &&
                latestOtp.getOtp().equals(inputOtp);
    }
}

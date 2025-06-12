package com.da.Attendance.service;

public interface OtpService {
    void generateAndSendOtp(String userEmail, String userId);
    boolean verifyOtp(String userId, String inputOtp);
}

package com.da.Attendance.service.Imp;

import com.da.Attendance.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MailServiceImp implements MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Override
    public void sendOtp(String toEmail, String otp) {
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
        String formattedExpiry = expiryTime.format(formatter);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom("lesin1666@gmail.com");
        message.setSubject("Mã OTP Khôi Phục Mật Khẩu");
        message.setText("Mã OTP của bạn là: " + otp +
                "\nOTP có hiệu lực trong 5 phút." +
                "\nVui lòng không chia sẻ mã này với bất kỳ ai.");
        mailSender.send(message);
    }
}

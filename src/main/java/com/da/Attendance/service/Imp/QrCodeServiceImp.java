package com.da.Attendance.service.Imp;

import com.da.Attendance.model.QrCode;
import com.da.Attendance.repository.QrCodeRepository;
import com.da.Attendance.service.QrCodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
@Service
public class QrCodeServiceImp implements QrCodeService {
    @Autowired
    private QrCodeRepository qrCodeRepository;
    @Override
    public String generateQRCode(String sessionId, double latitude, double longitude) throws WriterException {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            throw new IllegalArgumentException("sessionId cannot be empty");
        }
        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("invalid coordinates");
        }
        qrCodeRepository.deleteBySessionId(sessionId);
        Instant createdAt = Instant.now();
        Instant expiresAt = createdAt.plusSeconds(300);

        QrCode qrCode = new QrCode();
        qrCode.setSessionId(sessionId);
        qrCode.setCreatedAt(createdAt);
        qrCode.setExpiresAt(expiresAt);
        qrCode.setLatitude(latitude);
        qrCode.setLongitude(longitude);

        qrCodeRepository.save(qrCode);
        String qrContent = "sessionId=" + sessionId + "&expiresAt=" + expiresAt.toEpochMilli();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);
        try (ByteArrayOutputStream bass = new ByteArrayOutputStream()) {
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ImageIO.write(qrImage, "PNG", bass);
            return Base64.getEncoder().encodeToString(bass.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo QR Code", e);
        }
    }

    @Override
    public String generateQRCodeAttendance(String userId, double latitude, double longitude) throws WriterException {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("user ID cannot be empty");
        }
        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("invalid coordinates");
        }
        String qrContent = "userId=" + userId + "&latitude=" + latitude + "&longitude=" + longitude;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);
        try (ByteArrayOutputStream bass = new ByteArrayOutputStream()) {
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ImageIO.write(qrImage, "PNG", bass);
            return Base64.getEncoder().encodeToString(bass.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo QR Code", e);
        }
    }

    @Override
    public Optional<QrCode> getQRCodeBySession(String sessionId) {
        return qrCodeRepository.findBySessionId(sessionId);
    }
    @Scheduled(fixedRate = 30000) // 30s
    public void deleteExpiredQRCodes() {
        Instant now = Instant.now();
        qrCodeRepository.deleteByExpiresAtBefore(now);
    }
}

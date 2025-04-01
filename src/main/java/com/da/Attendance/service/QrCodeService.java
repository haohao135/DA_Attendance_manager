package com.da.Attendance.service;

import com.da.Attendance.model.QrCode;
import com.google.zxing.WriterException;

import java.util.Optional;

public interface QrCodeService {
    String generateQRCode(String sessionId, double latitude, double longitude) throws WriterException;
    Optional<QrCode> getQRCodeBySession(String sessionId);
}

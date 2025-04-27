package com.da.Attendance.service.Imp;

import com.da.Attendance.dto.request.AttendanceRecord.AddAttendanceRecordRequest;
import com.da.Attendance.model.AttendanceRecord;
import com.da.Attendance.model.AttendanceSession;
import com.da.Attendance.model.Event;
import com.da.Attendance.model.QrCode;
import com.da.Attendance.model.enums.AttendanceMethod;
import com.da.Attendance.model.enums.AttendanceStatus;
import com.da.Attendance.repository.AttendanceRecordRepository;
import com.da.Attendance.repository.AttendanceSessionRepository;
import com.da.Attendance.repository.EventRepository;
import com.da.Attendance.repository.QrCodeRepository;
import com.da.Attendance.service.AttendanceRecordService;
import com.da.Attendance.service.AttendanceSessionService;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceRecordServiceImp implements AttendanceRecordService {
    @Autowired
    private QrCodeRepository qrCodeRepository;
    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private AttendanceSessionRepository attendanceSessionRepository;


    @Override
    @Transactional
    public String scanAndRecordAttendance(String qrCodeBase64, String studentId, double userLatitude, double userLongitude)
            throws IOException, NotFoundException {
        if (qrCodeBase64 == null || qrCodeBase64.trim().isEmpty()) {
            throw new IllegalArgumentException("invalid QR code Base64 string");
        }
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("studentId cannot be empty");
        }
        if (userLatitude < -90 || userLatitude > 90 || userLongitude < -180 || userLongitude > 180) {
            throw new IllegalArgumentException("invalid user coordinates");
        }
        byte[] imageBytes = Base64.getDecoder().decode(qrCodeBase64);
        BufferedImage qrImage = javax.imageio.ImageIO.read(new ByteArrayInputStream(imageBytes));

        LuminanceSource source = new BufferedImageLuminanceSource(qrImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        MultiFormatReader reader = new MultiFormatReader();
        String qrContent = reader.decode(bitmap).getText();

        String sessionId = extractSessionId(qrContent);
        long expiresAtMillis = extractExpiresAt(qrContent);
        QrCode qrCode = qrCodeRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("QR code is invalid or does not exist"));
        Instant now = Instant.now();
        Instant expiresAt = Instant.ofEpochMilli(expiresAtMillis);
        if (now.isAfter(expiresAt)) {
            throw new IllegalArgumentException("QR code has expired");
        }
        double qrLatitude = qrCode.getLatitude();
        double qrLongitude = qrCode.getLongitude();
        double distance = calculateDistance(qrLatitude, qrLongitude, userLatitude, userLongitude);
        double maxDistanceMeters = 100;
        if (distance > maxDistanceMeters) {
            throw new IllegalArgumentException(
                    String.format("You are not in the correct roll call position (distance: %.2f meters)", distance));
        }
        AttendanceSession session = attendanceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("roll call does not exist"));
        if (session.getAttendanceRecordsStudentId() == null || !session.getAttendanceRecordsStudentId().contains(studentId)) {
            throw new IllegalArgumentException("you are not on the class list for this roll call.");
        }
        Optional<AttendanceRecord> existingRecord = attendanceRecordRepository
                .findByAttendanceSessionIdAndStudentId(sessionId, studentId);
        if (existingRecord.isPresent()) {
            throw new IllegalArgumentException("you have checked in for this session!");
        }
        AttendanceRecord attendance = new AttendanceRecord();
        attendance.setStudentId(studentId);
        attendance.setAttendanceSessionId(sessionId);
        attendance.setTimestamp(now);
        attendance.setLatitude(userLatitude);
        attendance.setLongitude(userLongitude);
        attendance.setMethod(AttendanceMethod.QR_CODE);
        attendance.setStatus(AttendanceStatus.PRESENT);
        attendanceRecordRepository.save(attendance);
        return "roll call successful!";
    }

    @Override
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000;
    }

    @Override
    public List<AttendanceRecord> getAbsences(String studentId) {
        return attendanceRecordRepository.findByStudentIdAndStatus(
                studentId, AttendanceStatus.ABSENT
        );
    }

    @Override
    public boolean add(AddAttendanceRecordRequest addAttendanceRecordRequest) {
        List<AttendanceRecord> records = addAttendanceRecordRequest.getAttendanceRecordsStudentId().stream().map(studentId -> {
            AttendanceRecord record = new AttendanceRecord();
            record.setStudentId(studentId);
            record.setAttendanceSessionId(addAttendanceRecordRequest.getAttendanceSessionId());
            record.setMethod(addAttendanceRecordRequest.getMethod());
            record.setStatus(addAttendanceRecordRequest.getStatus());
            record.setTimestamp(addAttendanceRecordRequest.getTimestamp());
            return record;
        }).collect(Collectors.toList());
        attendanceRecordRepository.saveAll(records);
        return true;
    }
    private String extractSessionId(String qrContent) {
        String[] parts = qrContent.split("&");
        for (String part : parts) {
            if (part.startsWith("sessionId=")) {
                return part.substring("sessionId=".length());
            }
        }
        throw new IllegalArgumentException("sessionId not found in QR code");
    }

    private long extractExpiresAt(String qrContent) {
        String[] parts = qrContent.split("&");
        for (String part : parts) {
            if (part.startsWith("expiresAt=")) {
                try {
                    return Long.parseLong(part.substring("expiresAt=".length()));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("expiresAt is invalid");
                }
            }
        }
        throw new IllegalArgumentException("expiresAt not found in QR code");
    }
}

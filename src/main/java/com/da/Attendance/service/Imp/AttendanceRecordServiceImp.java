package com.da.Attendance.service.Imp;

import com.da.Attendance.dto.request.AttendanceRecord.AddAttendanceRecordRequest;
import com.da.Attendance.dto.response.AttendanceRecordResponse.AttendanceResponse;
import com.da.Attendance.dto.response.User.UserAttendanceRecordResponse;
import com.da.Attendance.model.*;
import com.da.Attendance.model.enums.AttendanceMethod;
import com.da.Attendance.model.enums.AttendanceStatus;
import com.da.Attendance.repository.*;
import com.da.Attendance.service.AttendanceRecordService;
import com.da.Attendance.service.AttendanceSessionService;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserRepository userRepository;
    @Override
    @Transactional
    public AttendanceResponse scanAndRecordAttendance(String qrContent, String studentId, double userLatitude, double userLongitude)
            throws IOException, NotFoundException {
        if (qrContent == null || qrContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã QR không hợp lệ Chuỗi Base64");
        }
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("studentId không được để trống");
        }
        if (userLatitude < -90 || userLatitude > 90 || userLongitude < -180 || userLongitude > 180) {
            throw new IllegalArgumentException("tọa độ người dùng không hợp lệ");
        }

        String sessionId = extractSessionId(qrContent);
        long expiresAtMillis = extractExpiresAt(qrContent);
        QrCode qrCode = qrCodeRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Mã QR không hợp lệ hoặc không tồn tại"));
        Instant now = Instant.now();
        Instant expiresAt = Instant.ofEpochMilli(expiresAtMillis);
        if (now.isAfter(expiresAt)) {
            throw new IllegalArgumentException("Mã QR đã hết hạn");
        }
        double qrLatitude = qrCode.getLatitude();
        double qrLongitude = qrCode.getLongitude();
        double distance = calculateDistance(qrLatitude, qrLongitude, userLatitude, userLongitude);
        double maxDistanceMeters = 100;
        if (distance > maxDistanceMeters) {
            throw new IllegalArgumentException(
                    String.format("Bạn không ở đúng vị trí điểm danh (distance: %.2f meters)", distance));
        }
        AttendanceSession session = attendanceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("roll call does not exist"));
        if (session.getAttendanceRecordsStudentId() == null || !session.getAttendanceRecordsStudentId().contains(studentId)) {
            throw new IllegalArgumentException("you are not on the class list for this roll call.");
        }

        Optional<AttendanceRecord> existingRecord = attendanceRecordRepository
                .findByAttendanceSessionIdAndStudentId(sessionId, studentId);
        AttendanceRecord attendanceRecord = existingRecord
                .orElseThrow(() -> new IllegalArgumentException("Bạn không có trong buổi học này!"));
        if (attendanceRecord.getStatus() == AttendanceStatus.PRESENT) {
            throw new IllegalArgumentException("Bạn đã điểm danh rồi.");
        }
        attendanceRecord.setTimestamp(now);
        attendanceRecord.setLatitude(userLatitude);
        attendanceRecord.setLongitude(userLongitude);
        attendanceRecord.setMethod(AttendanceMethod.QR_CODE);
        attendanceRecord.setStatus(AttendanceStatus.PRESENT);
        attendanceRecordRepository.save(attendanceRecord);

        Optional<User> users = userRepository.findById(studentId);
        if(users.isEmpty()){
            throw new RuntimeException("user not exists");
        }
        User user = users.get();

        UserAttendanceRecordResponse userAttendanceRecordResponse = new UserAttendanceRecordResponse();
        userAttendanceRecordResponse.setAttendanceRecordId(attendanceRecord.getId());
        userAttendanceRecordResponse.setUser(user);
        userAttendanceRecordResponse.setStatus(AttendanceStatus.PRESENT);

        String destination = "/topic/attendance/" + sessionId;
        messagingTemplate.convertAndSend(destination, userAttendanceRecordResponse);
        return new AttendanceResponse("Điểm danh thành công!", attendanceRecord.getTimestamp());
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

    @Override
    public AttendanceRecord updateStatus(String id, AttendanceStatus attendanceStatus) {
        Optional<AttendanceRecord> attendanceRecords = attendanceRecordRepository.findById(id);
        if(attendanceRecords.isEmpty()){
            throw new RuntimeException("attendance record not found");
        }
        AttendanceRecord attendanceRecord = attendanceRecords.get();
        attendanceRecord.setStatus(attendanceStatus);
        return attendanceRecordRepository.save(attendanceRecord);
    }

    @Override
    public void addOne(AttendanceSession attendanceSession, String studentId) {
        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setAttendanceSessionId(attendanceSession.getId());
        attendanceRecord.setStudentId(studentId);
        attendanceRecord.setStatus(AttendanceStatus.ABSENT);
        attendanceRecord.setMethod(AttendanceMethod.QR_CODE);
        attendanceRecordRepository.save(attendanceRecord);
    }

    @Override
    public AttendanceRecord findById(String id) {
        Optional<AttendanceRecord> attendanceRecords = attendanceRecordRepository.findById(id);
        if(attendanceRecords.isEmpty()){
            throw new RuntimeException("attendanceRecord not found");
        }
        return attendanceRecords.get();
    }

    @Override
    public void deleteOne(AttendanceSession attendanceSession, String studentId) {
        Optional<AttendanceRecord> recordOpt = attendanceRecordRepository.findByAttendanceSessionIdAndStudentId(
                attendanceSession.getId(), studentId
        );

        recordOpt.ifPresent(attendanceRecordRepository::delete);
    }

    @Override
    public void deleteAllBySessionId(String sessionId) {
        attendanceRecordRepository.deleteByAttendanceSessionId(sessionId);
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

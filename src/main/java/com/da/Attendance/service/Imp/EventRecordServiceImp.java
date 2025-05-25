package com.da.Attendance.service.Imp;

import com.da.Attendance.dto.response.AttendanceRecordResponse.AttendanceResponse;
import com.da.Attendance.dto.response.User.UserAttendanceRecordResponse;
import com.da.Attendance.model.*;
import com.da.Attendance.model.enums.AttendanceMethod;
import com.da.Attendance.model.enums.AttendanceStatus;
import com.da.Attendance.repository.EventRecordRepository;
import com.da.Attendance.repository.EventRepository;
import com.da.Attendance.repository.QrCodeRepository;
import com.da.Attendance.repository.UserRepository;
import com.da.Attendance.service.EventRecordService;
import com.da.Attendance.service.EventService;
import com.google.zxing.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventRecordServiceImp implements EventRecordService {
    @Autowired
    private EventRecordRepository eventRecordRepository;
    @Autowired
    private QrCodeRepository qrCodeRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public AttendanceResponse scanAndRecordAttendance(String qrContent, String studentId, double userLatitude, double userLongitude) throws IOException, NotFoundException {
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
        Optional<Event> events = eventRepository.findById(sessionId);
        if(events.isEmpty()){
            throw new RuntimeException("event not found");
        }
        Event event = events.get();
        if (event.getParticipantIds() == null || !event.getParticipantIds().contains(studentId)) {
            throw new IllegalArgumentException("Bạn không có trong sự kiện này.");
        }
        Optional<EventRecord> existingEventRecord = eventRecordRepository.findByStudentIdAndEventId(studentId, sessionId);
        EventRecord eventRecord = existingEventRecord.orElseThrow(() -> new IllegalArgumentException("Bạn không có trong sự kiện này!"));
        if (eventRecord.getAttendanceStatus() == AttendanceStatus.PRESENT) {
            throw new IllegalArgumentException("Bạn đã điểm danh rồi.");
        }

        eventRecord.setTimeStamp(Instant.now());
        eventRecord.setMethod(AttendanceMethod.QR_CODE);
        eventRecord.setLatitude(userLatitude);
        eventRecord.setLongtitude(userLongitude);
        eventRecord.setAttendanceStatus(AttendanceStatus.PRESENT);
        eventRecordRepository.save(eventRecord);

        Optional<User> users = userRepository.findById(studentId);
        if(users.isEmpty()){
            throw new RuntimeException("user not exists");
        }
        User user = users.get();

        UserAttendanceRecordResponse userAttendanceRecordResponse = new UserAttendanceRecordResponse();
        userAttendanceRecordResponse.setAttendanceRecordId(eventRecord.getId());
        userAttendanceRecordResponse.setUser(user);
        userAttendanceRecordResponse.setStatus(AttendanceStatus.PRESENT);

        String destination = "/topic/attendance/" + sessionId;
        messagingTemplate.convertAndSend(destination, userAttendanceRecordResponse);
        return new AttendanceResponse("Điểm danh thành công!", eventRecord.getTimeStamp());
    }

    @Override
    public boolean add(Event event) {
        List<EventRecord> eventRecords = event.getParticipantIds().stream().map(studentId -> {
            EventRecord eventRecord = new EventRecord();
            eventRecord.setStudentId(studentId);
            eventRecord.setEventId(event.getId());
            eventRecord.setMethod(AttendanceMethod.QR_CODE);
            eventRecord.setAttendanceStatus(AttendanceStatus.ABSENT);
            return eventRecord;
        }).collect(Collectors.toList());
        eventRecordRepository.saveAll(eventRecords);
        return true;
    }

    @Override
    public boolean addOne(Event event, String studentId) {
        EventRecord eventRecord = new EventRecord();
        eventRecord.setStudentId(studentId);
        eventRecord.setEventId(event.getId());
        eventRecord.setMethod(AttendanceMethod.QR_CODE);
        eventRecord.setAttendanceStatus(AttendanceStatus.ABSENT);
        eventRecordRepository.save(eventRecord);
        return true;
    }

    @Override
    public EventRecord updateStatus(String id, AttendanceStatus attendanceStatus) {
        Optional<EventRecord> eventRecord = eventRecordRepository.findById(id);
        if(eventRecord.isEmpty()){
            throw new RuntimeException("event record not found");
        }
        EventRecord newEvent = eventRecord.get();
        newEvent.setAttendanceStatus(attendanceStatus);
        return eventRecordRepository.save(newEvent);
    }

    @Override
    public EventRecord findById(String id) {
        Optional<EventRecord> eventRecords = eventRecordRepository.findById(id);
        if(eventRecords.isEmpty()){
            throw new RuntimeException("event record not found");
        }
        return eventRecords.get();
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
}

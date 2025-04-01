package com.da.Attendance.service.Imp;

import com.da.Attendance.model.AttendanceRecord;
import com.da.Attendance.model.Event;
import com.da.Attendance.model.QrCode;
import com.da.Attendance.model.enums.AttendanceMethod;
import com.da.Attendance.model.enums.AttendanceStatus;
import com.da.Attendance.repository.AttendanceRecordRepository;
import com.da.Attendance.repository.EventRepository;
import com.da.Attendance.repository.QrCodeRepository;
import com.da.Attendance.service.AttendanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
@Service
public class AttendanceRecordServiceImp implements AttendanceRecordService {
    @Autowired
    private QrCodeRepository qrCodeRepository;
    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;
    @Autowired
    private EventRepository eventRepository;
    @Override
    public Map<String, String> validateAttendance(String sessionId, String studentId, double studentLatitude, double studentLongitude) {
        Optional<QrCode> qrCodeOpt = qrCodeRepository.findBySessionId(sessionId);

        if (qrCodeOpt.isEmpty()) {
            return Map.of("message", "QR Code is invalid or expired.");
        }

        QrCode qrCode = qrCodeOpt.get();
        Instant now = Instant.now();

        if (now.isAfter(qrCode.getExpiresAt())) {
            return Map.of("message", "QR Code has expired.");
        }

        double distance = calculateDistance(qrCode.getLatitude(), qrCode.getLongitude(), studentLatitude, studentLongitude);
        if (distance > 50) {
            return Map.of("message", "You are not near the classroom to take attendance.");
        }
        boolean alreadyCheckedIn = attendanceRecordRepository
                .existsByStudentIdAndAttendanceSessionId(studentId, sessionId);

        if (alreadyCheckedIn) {
            return Map.of("message", "You have checked in before.");
        }

        AttendanceRecord attendance = new AttendanceRecord();
        attendance.setStudentId(studentId);
        attendance.setAttendanceSessionId(sessionId);
        attendance.setTimestamp(now);
        attendance.setLatitude(studentLatitude);
        attendance.setLongitude(studentLongitude);
        attendance.setMethod(AttendanceMethod.QR_CODE);
        attendance.setStatus(AttendanceStatus.PRESENT);

        attendanceRecordRepository.save(attendance);

        return Map.of("message", "Roll call successful.");
    }

    @Override
    public Map<String, String> validateEventAttendance(String eventId, String studentId, double studentLatitude, double studentLongitude) {
        Optional<QrCode> qrCodeOpt = qrCodeRepository.findBySessionId(eventId);

        if (qrCodeOpt.isEmpty()) {
            return Map.of("message", "QR Code is invalid or expired.");
        }

        QrCode qrCode = qrCodeOpt.get();
        Instant now = Instant.now();

        if (now.isAfter(qrCode.getExpiresAt())) {
            return Map.of("message", "QR Code has expired.");
        }

        double distance = calculateDistance(qrCode.getLatitude(), qrCode.getLongitude(), studentLatitude, studentLongitude);
        if (distance > 50) {
            return Map.of("message", "You are not near the event to check in.");
        }

        boolean alreadyCheckedIn = attendanceRecordRepository
                .existsByStudentIdAndAttendanceSessionId(studentId, eventId);

        if (alreadyCheckedIn) {
            return Map.of("message", "You have checked in before.");
        }

        AttendanceRecord attendance = new AttendanceRecord();
        attendance.setStudentId(studentId);
        attendance.setAttendanceSessionId(eventId);
        attendance.setTimestamp(now);
        attendance.setLatitude(studentLatitude);
        attendance.setLongitude(studentLongitude);
        attendance.setMethod(AttendanceMethod.QR_CODE);
        attendance.setStatus(AttendanceStatus.PRESENT);

        attendanceRecordRepository.save(attendance);

        Optional<Event> eventOpt = eventRepository.findById(eventId);
        eventOpt.ifPresent(event -> {
            event.getParticipantIds().add(studentId);
            eventRepository.save(event);
        });

        return Map.of("message", "Roll call of successful events.");
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
}

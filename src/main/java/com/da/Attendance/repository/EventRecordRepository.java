package com.da.Attendance.repository;

import com.da.Attendance.model.EventRecord;
import com.da.Attendance.model.enums.AttendanceStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EventRecordRepository extends MongoRepository<EventRecord, String> {
    List<EventRecord> findByEventId(String eventId);
    List<EventRecord> findByEventIdAndAttendanceStatus(String eventId, AttendanceStatus attendanceStatus);
    List<EventRecord> findByStudentIdAndAttendanceStatus(String studentId, AttendanceStatus attendanceStatus);
    List<EventRecord> findByStudentIdAndAttendanceStatusIn(String studentId, List<AttendanceStatus> statuses);
    Optional<EventRecord> findByStudentIdAndEventId(String studentId, String eventId);
}

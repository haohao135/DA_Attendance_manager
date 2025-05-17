package com.da.Attendance.repository;

import com.da.Attendance.model.EventRecord;
import com.da.Attendance.model.enums.AttendanceStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRecordRepository extends MongoRepository<EventRecord, String> {
    List<EventRecord> findByEventIdAndAttendanceStatus(String eventId, AttendanceStatus attendanceStatus);
    List<EventRecord> findByStudentIdAndAttendanceStatus(String studentId, AttendanceStatus attendanceStatus);
    List<EventRecord> findByStudentIdAndAttendanceStatusIn(String studentId, List<AttendanceStatus> statuses);
}

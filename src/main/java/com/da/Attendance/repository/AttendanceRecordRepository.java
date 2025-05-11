package com.da.Attendance.repository;

import com.da.Attendance.model.AttendanceRecord;
import com.da.Attendance.model.enums.AttendanceMethod;
import com.da.Attendance.model.enums.AttendanceStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRecordRepository extends MongoRepository<AttendanceRecord, String> {
    List<AttendanceRecord> findByAttendanceSessionIdAndStatus(String attendanceSessionId, AttendanceStatus status);
    List<AttendanceRecord> findByAttendanceSessionIdAndStatusIn(String sessionId, List<AttendanceStatus> statuses);
    boolean existsByStudentIdAndAttendanceSessionId(String studentId, String attendanceSessionId);
    List<AttendanceRecord> findByStudentIdAndStatus(String studentId, AttendanceStatus status);
    Optional<AttendanceRecord> findByAttendanceSessionIdAndStudentId(String attendanceSessionId, String studentId);
}

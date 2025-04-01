package com.da.Attendance.repository;

import com.da.Attendance.model.AttendanceRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRecordRepository extends MongoRepository<AttendanceRecord, String> {
    boolean existsByStudentIdAndAttendanceSessionId(String studentId, String attendanceSessionId);
}

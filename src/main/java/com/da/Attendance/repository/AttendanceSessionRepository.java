package com.da.Attendance.repository;

import com.da.Attendance.model.AttendanceSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceSessionRepository extends MongoRepository<AttendanceSession, String> {
    List<AttendanceSession> findByAttendanceRecordsStudentIdContains(String studentId);
    List<AttendanceSession> findByClassIdIn(List<String> classIds);
    List<AttendanceSession> findByClassId(String classId);
    List<AttendanceSession> findByClassIdOrderByDateTimeAsc(String classId);

}

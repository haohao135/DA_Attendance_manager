package com.da.Attendance.model;

import com.da.Attendance.model.enums.AttendanceMethod;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document
public class AttendanceSession {
    @Id
    private String id;
    private String classId;
    private Instant dateTime;
    private AttendanceMethod method;
    List<String> attendanceRecordsStudentId;
}

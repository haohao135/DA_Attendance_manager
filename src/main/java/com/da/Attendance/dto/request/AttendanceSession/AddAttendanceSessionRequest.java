package com.da.Attendance.dto.request.AttendanceSession;

import com.da.Attendance.model.enums.AttendanceMethod;

import java.time.Instant;
import java.util.List;

public class AddAttendanceSessionRequest {
    private String classId;
    private String className;
    private Instant dateTime;
    private AttendanceMethod method;
    List<String> attendanceRecordsStudentId;

    public AddAttendanceSessionRequest() {
    }

    public AddAttendanceSessionRequest(String classId, String className, Instant dateTime, AttendanceMethod method, List<String> attendanceRecordsStudentId) {
        this.classId = classId;
        this.className = className;
        this.dateTime = dateTime;
        this.method = method;
        this.attendanceRecordsStudentId = attendanceRecordsStudentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Instant getDateTime() {
        return dateTime;
    }

    public void setDateTime(Instant dateTime) {
        this.dateTime = dateTime;
    }

    public AttendanceMethod getMethod() {
        return method;
    }

    public void setMethod(AttendanceMethod method) {
        this.method = method;
    }

    public List<String> getAttendanceRecordsStudentId() {
        return attendanceRecordsStudentId;
    }

    public void setAttendanceRecordsStudentId(List<String> attendanceRecordsStudentId) {
        this.attendanceRecordsStudentId = attendanceRecordsStudentId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}

package com.da.Attendance.dto.request.AttendanceRecord;

import com.da.Attendance.model.enums.AttendanceMethod;
import com.da.Attendance.model.enums.AttendanceStatus;

import java.time.Instant;
import java.util.List;

public class AddAttendanceRecordRequest {
    private String attendanceSessionId;
    private AttendanceMethod method;
    private AttendanceStatus status;
    private Instant timestamp;
    List<String> attendanceRecordsStudentId;

    public AddAttendanceRecordRequest() {
    }

    public AddAttendanceRecordRequest(String attendanceSessionId, AttendanceMethod method, AttendanceStatus status, Instant timestamp, List<String> attendanceRecordsStudentId) {
        this.attendanceSessionId = attendanceSessionId;
        this.method = method;
        this.status = status;
        this.timestamp = timestamp;
        this.attendanceRecordsStudentId = attendanceRecordsStudentId;
    }
    public String getAttendanceSessionId() {
        return attendanceSessionId;
    }

    public void setAttendanceSessionId(String attendanceSessionId) {
        this.attendanceSessionId = attendanceSessionId;
    }

    public AttendanceMethod getMethod() {
        return method;
    }

    public void setMethod(AttendanceMethod method) {
        this.method = method;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getAttendanceRecordsStudentId() {
        return attendanceRecordsStudentId;
    }

    public void setAttendanceRecordsStudentId(List<String> attendanceRecordsStudentId) {
        this.attendanceRecordsStudentId = attendanceRecordsStudentId;
    }
}

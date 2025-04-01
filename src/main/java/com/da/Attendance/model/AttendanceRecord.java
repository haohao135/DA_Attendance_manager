package com.da.Attendance.model;

import com.da.Attendance.model.enums.AttendanceMethod;
import com.da.Attendance.model.enums.AttendanceStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJson;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document
public class AttendanceRecord {
    @Id
    private String id;
    private String studentId;
    private String attendanceSessionId;
    private AttendanceMethod method;
    private AttendanceStatus status;
    private Instant timestamp;
    private double latitude;
    private double longitude;

    public AttendanceRecord() {
    }

    public AttendanceRecord(String id, String studentId, String attendanceSessionId, AttendanceMethod method, AttendanceStatus status, Instant timestamp, double latitude, double longitude) {
        this.id = id;
        this.studentId = studentId;
        this.attendanceSessionId = attendanceSessionId;
        this.method = method;
        this.status = status;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

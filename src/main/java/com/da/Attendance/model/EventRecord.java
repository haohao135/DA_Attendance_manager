package com.da.Attendance.model;

import com.da.Attendance.model.enums.AttendanceMethod;
import com.da.Attendance.model.enums.AttendanceStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
public class EventRecord {
    @Id
    private String id;
    private String studentId;
    private String eventId;
    private AttendanceMethod method;
    private AttendanceStatus attendanceStatus;
    private Instant timeStamp = Instant.now();
    private double latitude;
    private double longtitude;

    public EventRecord() {
    }

    public EventRecord(String id, String studentId, String eventId, AttendanceMethod method, AttendanceStatus attendanceStatus, Instant timeStamp, double latitude, double longtitude) {
        this.id = id;
        this.studentId = studentId;
        this.eventId = eventId;
        this.method = method;
        this.attendanceStatus = attendanceStatus;
        this.timeStamp = timeStamp;
        this.latitude = latitude;
        this.longtitude = longtitude;
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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public AttendanceMethod getMethod() {
        return method;
    }

    public void setMethod(AttendanceMethod method) {
        this.method = method;
    }

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
}

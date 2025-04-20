package com.da.Attendance.model;

import com.da.Attendance.model.enums.AttendanceMethod;
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
    private Instant timestamp;
    private double latitude;
    private double longtitude;

    public EventRecord() {
    }
}

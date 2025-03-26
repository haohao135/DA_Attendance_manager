package com.da.Attendance.model;

import com.da.Attendance.model.enums.AttendanceMethod;
import com.da.Attendance.model.enums.AttendanceStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJson;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
public class EventAttendance {
    @Id
    private String id;
    private String studentId;
    private AttendanceMethod method;
    //private AttendanceStatus status;
    private Instant timestamp;
    private GeoJson location;
}

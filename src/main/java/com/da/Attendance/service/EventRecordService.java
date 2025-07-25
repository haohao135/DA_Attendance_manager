package com.da.Attendance.service;

import com.da.Attendance.dto.response.AttendanceRecordResponse.AttendanceResponse;
import com.da.Attendance.model.Event;
import com.da.Attendance.model.EventRecord;
import com.da.Attendance.model.enums.AttendanceStatus;
import com.google.zxing.NotFoundException;

import java.io.IOException;

public interface EventRecordService {
    AttendanceResponse scanAndRecordAttendance(String qrContent, String studentId, double userLatitude, double userLongitude) throws IOException, NotFoundException;
    boolean add(Event event);
    boolean addOne(Event event, String studentId);
    EventRecord updateStatus(String id, AttendanceStatus attendanceStatus);
    EventRecord findById(String id);
    void removeOne(Event event, String studentId);
    AttendanceResponse scanFromStudent(String qrContent, String eventId, double expectedLatitude,
                                       double expectedLongitude);
}

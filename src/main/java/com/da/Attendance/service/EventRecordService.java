package com.da.Attendance.service;

import com.da.Attendance.model.Event;
import com.da.Attendance.model.EventRecord;
import com.da.Attendance.model.enums.AttendanceStatus;

public interface EventRecordService {
    boolean add(Event event);
    boolean addOne(Event event, String studentId);
    EventRecord updateStatus(String id, AttendanceStatus attendanceStatus);
}

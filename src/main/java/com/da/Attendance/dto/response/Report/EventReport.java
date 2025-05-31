package com.da.Attendance.dto.response.Report;

import com.da.Attendance.model.EventRecord;
import com.da.Attendance.model.User;

import java.util.List;

public class EventReport {
    List<User> users;
    List<EventRecord> eventRecords;
}

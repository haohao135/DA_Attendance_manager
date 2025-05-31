package com.da.Attendance.dto.response.Report;

import com.da.Attendance.model.AttendanceRecord;
import com.da.Attendance.model.AttendanceSession;
import com.da.Attendance.model.User;

import java.util.List;

public class ClassReport {
    List<AttendanceSession> attendanceSessions;
    List<AttendanceRecord> attendanceRecords;
    List<User> users;

    public ClassReport() {
    }

    public ClassReport(List<AttendanceSession> attendanceSessions, List<AttendanceRecord> attendanceRecords, List<User> users) {
        this.attendanceSessions = attendanceSessions;
        this.attendanceRecords = attendanceRecords;
        this.users = users;
    }
}

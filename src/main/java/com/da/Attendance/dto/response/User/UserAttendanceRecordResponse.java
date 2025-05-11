package com.da.Attendance.dto.response.User;

import com.da.Attendance.model.User;
import com.da.Attendance.model.enums.AttendanceStatus;

public class UserAttendanceRecordResponse {
    private String attendanceRecordId;
    private User user;
    private AttendanceStatus status;

    public UserAttendanceRecordResponse() {
    }

    public UserAttendanceRecordResponse(String attendanceRecordId, User user, AttendanceStatus status) {
        this.attendanceRecordId = attendanceRecordId;
        this.user = user;
        this.status = status;
    }

    public String getAttendanceRecordId() {
        return attendanceRecordId;
    }

    public void setAttendanceRecordId(String attendanceRecordId) {
        this.attendanceRecordId = attendanceRecordId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
}

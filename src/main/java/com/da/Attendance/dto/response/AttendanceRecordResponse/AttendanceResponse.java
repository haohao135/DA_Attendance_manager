package com.da.Attendance.dto.response.AttendanceRecordResponse;

import java.time.Instant;

public class AttendanceResponse {
    private String message;
    private Instant timestamp;

    public AttendanceResponse() {
    }

    public AttendanceResponse(String message, Instant timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}

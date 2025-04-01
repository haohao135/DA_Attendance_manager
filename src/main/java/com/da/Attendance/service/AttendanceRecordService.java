package com.da.Attendance.service;

import java.util.Map;

public interface AttendanceRecordService {
    Map<String, String> validateAttendance(String sessionId, String studentId, double studentLatitude, double studentLongitude);
    Map<String, String> validateEventAttendance(String eventId, String studentId, double studentLatitude, double studentLongitude);
    double calculateDistance(double lat1, double lon1, double lat2, double lon2);
}

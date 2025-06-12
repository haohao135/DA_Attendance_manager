package com.da.Attendance.service;

import com.da.Attendance.dto.request.AttendanceRecord.AddAttendanceRecordRequest;
import com.da.Attendance.dto.response.AttendanceRecordResponse.AttendanceResponse;
import com.da.Attendance.model.AttendanceRecord;
import com.da.Attendance.model.AttendanceSession;
import com.da.Attendance.model.enums.AttendanceStatus;
import com.google.zxing.NotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AttendanceRecordService {
    AttendanceResponse scanAndRecordAttendance(String qrContent, String studentId, double userLatitude, double userLongitude) throws IOException, NotFoundException;
    double calculateDistance(double lat1, double lon1, double lat2, double lon2);
    List<AttendanceRecord> getAbsences(String studentId);
    boolean add(AddAttendanceRecordRequest addAttendanceRecordRequest);
    AttendanceRecord updateStatus(String id, AttendanceStatus attendanceStatus);
    void addOne(AttendanceSession attendanceSession, String studentId);
    AttendanceRecord findById(String id);
    void deleteOne(AttendanceSession attendanceSession, String studentId);
    void deleteAllBySessionId(String sessionId);
}

package com.da.Attendance.service;

import com.da.Attendance.dto.request.AttendanceRecord.AddAttendanceRecordRequest;
import com.da.Attendance.model.AttendanceRecord;
import com.da.Attendance.model.enums.AttendanceStatus;
import com.google.zxing.NotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AttendanceRecordService {
    String scanAndRecordAttendance(String qrCodeBase64, String studentId, double userLatitude, double userLongitude) throws IOException, NotFoundException;
    double calculateDistance(double lat1, double lon1, double lat2, double lon2);
    List<AttendanceRecord> getAbsences(String studentId);
    boolean add(AddAttendanceRecordRequest addAttendanceRecordRequest);
    AttendanceRecord updateStatus(String id, AttendanceStatus attendanceStatus);
}

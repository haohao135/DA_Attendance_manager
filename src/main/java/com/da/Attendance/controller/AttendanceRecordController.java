package com.da.Attendance.controller;

import com.da.Attendance.dto.response.ApiResponse;
import com.da.Attendance.model.AttendanceRecord;
import com.da.Attendance.model.Classroom;
import com.da.Attendance.model.enums.AttendanceStatus;
import com.da.Attendance.service.AttendanceRecordService;
import com.google.zxing.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance-record")
@CrossOrigin("*")
public class AttendanceRecordController {
    @Autowired
    private AttendanceRecordService attendanceRecordService;
    @Autowired
    private AttendanceRecordService attendanceService;

    @PostMapping("/scan")
    public ResponseEntity<String> scanAndRecordAttendance(
            @RequestParam String qrCodeBase64,
            @RequestParam String studentId,
            @RequestParam double latitude,
            @RequestParam double longitude) {
        try {
            String result = attendanceRecordService.scanAndRecordAttendance(qrCodeBase64, studentId, latitude, longitude);
            return ResponseEntity.ok(result);
        } catch (IOException | NotFoundException e) {
            return ResponseEntity.badRequest().body("Error when scanning QR code:" + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/update/status")
    public ResponseEntity<ApiResponse> scanAndRecordAttendance(
            @RequestParam String id, @RequestParam AttendanceStatus attendanceStatus) {
        try{
            AttendanceRecord attendanceRecord = attendanceRecordService.updateStatus(id, attendanceStatus);
            return ResponseEntity.ok(new ApiResponse("Update attendance record success", attendanceRecord));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update attendance record failed " + e.getMessage(), null));
        }
    }
}

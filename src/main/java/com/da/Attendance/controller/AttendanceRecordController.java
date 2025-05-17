package com.da.Attendance.controller;

import com.da.Attendance.dto.request.QrCode.QRScanRequest;
import com.da.Attendance.dto.response.ApiResponse;
import com.da.Attendance.dto.response.AttendanceRecordResponse.AttendanceResponse;
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
    @PostMapping("/scan")
    public ResponseEntity<ApiResponse> scanAndRecordAttendance(
            @RequestBody QRScanRequest qrScanRequest) {
        try{
            AttendanceResponse result = attendanceRecordService.
                    scanAndRecordAttendance(qrScanRequest.getQrCode(), qrScanRequest.getStudentId(),
                            qrScanRequest.getLatitude(), qrScanRequest.getLongitude());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Scan attendance record success", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Invalid input: " + e.getMessage(), null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("QR code not found or unreadable", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Unexpected error: " + e.getMessage(), null));
        }
    }
    @PostMapping("/update/status")
    public ResponseEntity<ApiResponse> updateStatus(
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

package com.da.Attendance.controller;

import com.da.Attendance.dto.response.ApiResponse;
import com.da.Attendance.dto.response.AttendanceRecordResponse.AttendanceResponse;
import com.da.Attendance.model.AttendanceRecord;
import com.da.Attendance.model.EventRecord;
import com.da.Attendance.model.enums.AttendanceStatus;
import com.da.Attendance.service.EventRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event-record")
@CrossOrigin("*")
public class EventRecordController {
    @Autowired
    private EventRecordService eventRecordService;
    @PostMapping("/update/status")
    public ResponseEntity<ApiResponse> updateStatus(
            @RequestParam String id, @RequestParam AttendanceStatus attendanceStatus) {
        try{
            EventRecord EventRecord = eventRecordService.updateStatus(id, attendanceStatus);
            return ResponseEntity.ok(new ApiResponse("Update event record success", EventRecord));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update event record failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/scan")
    public ResponseEntity<ApiResponse> scanQrCodeFromStudent(
            @RequestParam String qrContent,
            @RequestParam String eventId,
            @RequestParam double expectedLatitude,
            @RequestParam double expectedLongitude) {
        try {
            AttendanceResponse attendanceResponse = eventRecordService
                    .scanFromStudent(qrContent, eventId, expectedLatitude, expectedLongitude);
            return ResponseEntity.ok(new ApiResponse("Scan QR thành công", attendanceResponse));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Dữ liệu không hợp lệ: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Lỗi hệ thống khi quét QR: " + e.getMessage(), null));
        }
    }

}

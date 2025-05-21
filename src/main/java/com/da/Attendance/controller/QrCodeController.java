package com.da.Attendance.controller;

import com.da.Attendance.dto.request.QrCode.QRScanRequest;
import com.da.Attendance.dto.response.ApiResponse;
import com.da.Attendance.dto.response.AttendanceRecordResponse.AttendanceResponse;
import com.da.Attendance.model.AttendanceRecord;
import com.da.Attendance.model.EventRecord;
import com.da.Attendance.model.Notification;
import com.da.Attendance.repository.AttendanceRecordRepository;
import com.da.Attendance.repository.EventRecordRepository;
import com.da.Attendance.service.AttendanceRecordService;
import com.da.Attendance.service.EventRecordService;
import com.da.Attendance.service.QrCodeService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/qrcode")
@CrossOrigin("*")
public class QrCodeController {
    @Autowired
    private QrCodeService qrCodeService;
    @Autowired
    private AttendanceRecordService attendanceRecordService;
    @Autowired
    private EventRecordService eventRecordService;
    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;
    @Autowired
    private EventRecordRepository eventRecordRepository;

    @PostMapping("/generate-classroom")
    public ResponseEntity<ApiResponse> generateQRCodeClassroom(@RequestParam String sessionId,
                                              @RequestParam double latitude,
                                              @RequestParam double longitude) throws WriterException {
        try{
            String qrCodeBase64 = qrCodeService.generateQRCode(sessionId, latitude, longitude);
            return ResponseEntity.ok(new ApiResponse("Generate qrCode success", qrCodeBase64));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Generate qrCode failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/check")
    public ResponseEntity<ApiResponse> handleQrcode(@RequestBody QRScanRequest qrScanRequest) throws WriterException {
        try{
            String sessionId;
            try {
                sessionId = extractSessionId(qrScanRequest.getQrCode());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("Invalid QR code format: " + e.getMessage(), null));
            }
            System.out.println(qrScanRequest.getQrCode() + "code nè");
            System.out.println(sessionId + "id nè");
            AttendanceRecord attendanceRecord = attendanceRecordRepository.findById(sessionId).orElse(null);
            if(attendanceRecord != null){
                AttendanceResponse attendanceResponse = attendanceRecordService.scanAndRecordAttendance(
                        qrScanRequest.getQrCode(),
                        qrScanRequest.getStudentId(),
                        qrScanRequest.getLatitude(),
                        qrScanRequest.getLongitude()
                );
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse("Scan attendance record success", attendanceResponse));
            }
            EventRecord eventRecord = eventRecordRepository.findById(sessionId).orElse(null);
            if(eventRecord != null){
                AttendanceResponse attendanceResponse = eventRecordService.scanAndRecordAttendance(
                        qrScanRequest.getQrCode(),
                        qrScanRequest.getStudentId(),
                        qrScanRequest.getLatitude(),
                        qrScanRequest.getLongitude()
                );
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse("Scan event record success", attendanceResponse));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Invalid or expired QR code", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Scan qrCode failed " + e.getMessage(), null));
        }
    }
    public String extractSessionId(String qrCode) {
        if (qrCode == null) return null;
        String[] parts = qrCode.split("&");
        for (String part : parts) {
            if (part.startsWith("sessionId=")) {
                return part.substring("sessionId=".length());
            }
        }
        return null;
    }

}

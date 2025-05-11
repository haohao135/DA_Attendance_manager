package com.da.Attendance.controller;

import com.da.Attendance.dto.response.ApiResponse;
import com.da.Attendance.model.Notification;
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
}

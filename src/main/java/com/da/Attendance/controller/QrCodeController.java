package com.da.Attendance.controller;

import com.da.Attendance.service.QrCodeService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/qrcode")
@CrossOrigin("*")
public class QrCodeController {
    @Autowired
    private QrCodeService qrCodeService;

    @PostMapping("/generate")
    public Map<String, String> generateQRCode(@RequestParam String sessionId,
                                              @RequestParam double latitude,
                                              @RequestParam double longitude) throws WriterException {
        String qrCodeBase64 = qrCodeService.generateQRCode(sessionId, latitude, longitude);
        return Map.of("qrCode", qrCodeBase64);
    }
}

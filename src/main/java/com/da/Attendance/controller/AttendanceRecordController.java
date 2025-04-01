package com.da.Attendance.controller;

import com.da.Attendance.service.AttendanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/attendance-record")
@CrossOrigin("*")
public class AttendanceRecordController {
    @Autowired
    private AttendanceRecordService attendanceService;

    @PostMapping("/class/check-in")
    public ResponseEntity<Map<String, String>> validateAttendance(@RequestParam String sessionId,
                                                  @RequestParam String studentId,
                                                  @RequestParam double latitude,
                                                  @RequestParam double longitude) {
        return ResponseEntity.ok(attendanceService.validateAttendance(sessionId, studentId, latitude, longitude));
    }
    @PostMapping("/event/check-in")
    public ResponseEntity<Map<String, String>> checkInEvent(@RequestParam String eventId,
                                                            @RequestParam String studentId,
                                                            @RequestParam double latitude,
                                                            @RequestParam double longitude) {
        Map<String, String> response = attendanceService.validateEventAttendance(eventId, studentId, latitude, longitude);
        return ResponseEntity.ok(response);
    }
}

package com.da.Attendance.controller;

import com.da.Attendance.dto.response.ApiResponse;
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
}

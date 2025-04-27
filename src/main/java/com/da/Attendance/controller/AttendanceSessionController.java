package com.da.Attendance.controller;

import com.da.Attendance.dto.request.AttendanceSession.AddAttendanceSessionRequest;
import com.da.Attendance.dto.response.ApiResponse;
import com.da.Attendance.dto.response.AttendanceSession.AttendanceSessionScheduleResponse;
import com.da.Attendance.model.AttendanceSession;
import com.da.Attendance.model.Classroom;
import com.da.Attendance.service.AttendanceSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance-session")
@CrossOrigin("*")
public class AttendanceSessionController {
    @Autowired
    private AttendanceSessionService attendanceSessionService;
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createAttendanceSession(@RequestBody AddAttendanceSessionRequest addAttendanceSessionRequest){
        try{
            AttendanceSession attendanceSession = attendanceSessionService.add(addAttendanceSessionRequest);
            return ResponseEntity.ok(new ApiResponse("Add attendance session success", attendanceSession));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Add attendance session failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/get-absence")
    public ResponseEntity<ApiResponse> getAttendanceSessionAbsence(@RequestParam String studentId){
        try{
            List<AttendanceSession> attendanceSessions = attendanceSessionService.getAttendanceSessionAbsences(studentId);
            return ResponseEntity.ok(new ApiResponse("Get attendance session absence success", attendanceSessions));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Get attendance session absence failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/get-schedule")
    public ResponseEntity<ApiResponse> getAttendanceSessionSchedule(@RequestParam String studentId){
        try{
            List<AttendanceSessionScheduleResponse> attendanceSessions = attendanceSessionService.getAttendanceSessionScheduleByUserId(studentId);
            return ResponseEntity.ok(new ApiResponse("Get attendance session schedule success", attendanceSessions));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Get attendance session schedule failed " + e.getMessage(), null));
        }
    }
}

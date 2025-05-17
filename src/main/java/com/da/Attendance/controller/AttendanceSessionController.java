package com.da.Attendance.controller;

import com.da.Attendance.dto.request.AttendanceSession.AddAttendanceSessionRequest;
import com.da.Attendance.dto.request.AttendanceSession.UpdateAttendanceSessionRequest;
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
    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateAttendanceSession(@RequestParam String id, @RequestBody UpdateAttendanceSessionRequest updateAttendanceSessionRequest){
        try{
            AttendanceSession attendanceSession = attendanceSessionService.update(id, updateAttendanceSessionRequest);
            return ResponseEntity.ok(new ApiResponse("Update attendance session success", attendanceSession));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update attendance session failed " + e.getMessage(), null));
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
    @GetMapping("/get/teacher")
    public ResponseEntity<ApiResponse> getAttendanceSessionByTeacherId(@RequestParam String teacherId){
        try{
            List<AttendanceSessionScheduleResponse> attendanceSessions = attendanceSessionService.getAttendanceSessionsByTeacherId(teacherId);
            return ResponseEntity.ok(new ApiResponse("Get attendance session schedule success", attendanceSessions));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Get attendance session schedule failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/get/class")
    public ResponseEntity<ApiResponse> getAttendanceSessionsByClassId(@RequestParam String classId){
        try{
            List<AttendanceSession> attendanceSessions = attendanceSessionService.getAttendanceSessionsByClassId(classId);
            return ResponseEntity.ok(new ApiResponse("Get attendance session schedule success", attendanceSessions));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Get attendance session schedule failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> delete(@RequestParam String id){
        try{
            attendanceSessionService.delete(id);
            return ResponseEntity.ok(new ApiResponse("Delete attendance session success", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Delete attendance session failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/add-student")
    public ResponseEntity<ApiResponse> addOneStudent(@RequestParam String id, @RequestParam String studentId){
        try{
            AttendanceSession attendanceSession = attendanceSessionService.addOneStudent(id, studentId);
            return ResponseEntity.ok(new ApiResponse("Add student to attendance session success", attendanceSession));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Add student to attendance session failed " + e.getMessage(), null));
        }
    }
}

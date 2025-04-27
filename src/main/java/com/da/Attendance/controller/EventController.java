package com.da.Attendance.controller;

import com.da.Attendance.dto.request.Classroom.AddClassroomRequest;
import com.da.Attendance.dto.request.Event.AddEventRequest;
import com.da.Attendance.dto.request.Event.AddStudentsRequest;
import com.da.Attendance.dto.response.ApiResponse;
import com.da.Attendance.model.Classroom;
import com.da.Attendance.model.Event;
import com.da.Attendance.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")
@CrossOrigin("*")
public class EventController {
    @Autowired
    EventService eventService;
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addEvent(@RequestBody AddEventRequest addEventRequest){
        try{
            Event event = eventService.addEvent(addEventRequest);
            return ResponseEntity.ok(new ApiResponse("Add event success", event));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Add event failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/get/student")
    public ResponseEntity<ApiResponse> getByStudentId(@RequestParam String studentId){
        try{
            List<Event> events = eventService.getEventByStudentId(studentId);
            return ResponseEntity.ok(new ApiResponse("Get events success", events));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Get events failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/add-student")
    public ResponseEntity<ApiResponse> addStudent(@RequestParam String id, @RequestParam String studentId){
        try{
            Event event = eventService.addStudent(id, studentId);
            return ResponseEntity.ok(new ApiResponse("Add student success", event));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Add student failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/add-students")
    public ResponseEntity<ApiResponse> addStudents(@RequestBody AddStudentsRequest addStudentsRequest){
        try{
            Event event = eventService.addStudentList(addStudentsRequest);
            return ResponseEntity.ok(new ApiResponse("Add students success", event));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Add students failed " + e.getMessage(), null));
        }
    }
}

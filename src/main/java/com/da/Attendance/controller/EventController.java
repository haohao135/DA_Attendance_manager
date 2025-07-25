package com.da.Attendance.controller;

import com.da.Attendance.dto.request.Classroom.AddClassroomRequest;
import com.da.Attendance.dto.request.Event.AddEventRequest;
import com.da.Attendance.dto.request.Event.AddStudentsRequest;
import com.da.Attendance.dto.request.Event.UpdateEventRequest;
import com.da.Attendance.dto.response.ApiResponse;
import com.da.Attendance.model.Classroom;
import com.da.Attendance.model.Event;
import com.da.Attendance.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
    @GetMapping("/get/staff")
    public ResponseEntity<ApiResponse> getAllEventsByOrganizerId(@RequestParam String organizerId){
        try{
            List<Event> events = eventService.getAllEventsByOrganizerId(organizerId);
            return ResponseEntity.ok(new ApiResponse("Get events success", events));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Get events failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/get/staff/upcoming")
    public ResponseEntity<ApiResponse> getUpcomingEventsByOrganizerId(@RequestParam String organizerId){
        try{
            List<Event> events = eventService.getUpcomingEventsByOrganizerId(organizerId);
            return ResponseEntity.ok(new ApiResponse("Get events success", events));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Get events failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/get/staff/past")
    public ResponseEntity<ApiResponse> getPastEventsByOrganizerId(@RequestParam String organizerId){
        try{
            List<Event> events = eventService.getPastEventsByOrganizerId(organizerId);
            return ResponseEntity.ok(new ApiResponse("Get events success", events));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Get events failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllEvent(){
        try{
            List<Event> events = eventService.getAllEvent();
            return ResponseEntity.ok(new ApiResponse("Get events success", events));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Get events failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateEvent(@RequestParam String id, @RequestBody UpdateEventRequest updateEventRequest){
        try{
            Event event = eventService.updateEvent(id, updateEventRequest);
            return ResponseEntity.ok(new ApiResponse("Update event success", event));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update event failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> deleteEvent(@RequestParam String id){
        try{
            eventService.deleteEvent(id);
            return ResponseEntity.ok(new ApiResponse("Delete event success", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Delete event failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/get")
    public ResponseEntity<ApiResponse> getById(@RequestParam String id){
        try{
            Event event = eventService.findEventById(id);
            return ResponseEntity.ok(new ApiResponse("Get event success", event));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Get event failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/report")
    public ResponseEntity<byte[]> downloadAttendanceReport(@RequestParam String eventId) throws IOException {
        ByteArrayInputStream stream = eventService.exportEventAttendanceReport(eventId);
        if (stream == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        String filename = "event_report_" + eventId + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(stream.readAllBytes());
    }
    @PostMapping("/delete-student")
    public ResponseEntity<ApiResponse> deleteOneStudent(@RequestParam String id, @RequestParam String studentId){
        try{
            eventService.removeStudent(id, studentId);
            return ResponseEntity.ok(new ApiResponse("Delete student from event success", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Delete student from event failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/import-students")
    public ResponseEntity<ApiResponse> importStudentByCsv(
            @RequestParam String id,
            @RequestParam("file") MultipartFile file) {
        try {
            eventService.importStudentsFromCSV(id, file);
            return ResponseEntity.ok(
                    new ApiResponse("Import students success", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Import students failed: " + e.getMessage(), null));
        }
    }
}

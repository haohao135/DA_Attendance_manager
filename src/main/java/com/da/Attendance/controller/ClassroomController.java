package com.da.Attendance.controller;

import com.da.Attendance.dto.response.ApiResponse;
import com.da.Attendance.model.Classroom;
import com.da.Attendance.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/class")
@CrossOrigin("*")
public class ClassroomController {
    @Autowired
    private ClassroomService classroomService;
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getClass(@PathVariable String id){
        try{
            Classroom classroom = classroomService.findClassById(id);
            return ResponseEntity.ok(new ApiResponse("Get class success", classroom));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Get class failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/student")
    public ResponseEntity<ApiResponse> getByStudentId(@RequestParam String id){
        try{
            List<Classroom> classrooms = classroomService.findByStudentId(id);
            return ResponseEntity.ok(new ApiResponse("Get class success", classrooms));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Get class failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addClass(@RequestParam String className,
                                                @RequestParam String classId){
        try{
            Classroom classroom = classroomService.addClass(className, classId);
            return ResponseEntity.ok(new ApiResponse("Add class success", classroom));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Add class failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/{id}/add-teacher")
    public ResponseEntity<ApiResponse> addTeacher(@PathVariable String id, @RequestParam String teacherId){
        try{
            Classroom classroom = classroomService.addTeacher(id, teacherId);
            return ResponseEntity.ok(new ApiResponse("Add teacher success", classroom));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Add teacher failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/{id}/update-name")
    public ResponseEntity<ApiResponse> updateClassName(@PathVariable String id, @RequestParam String className){
        try{
            Classroom classroom = classroomService.updateClassName(id, className);
            return ResponseEntity.ok(new ApiResponse("Update class name success", classroom));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update class name failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/{id}/update-teacher")
    public ResponseEntity<ApiResponse> updateTeacher(@PathVariable String id, @RequestParam String teacherId){
        try{
            Classroom classroom = classroomService.updateTeacher(id, teacherId);
            return ResponseEntity.ok(new ApiResponse("Update teacher success", classroom));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update teacher failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/{id}/add-student")
    public ResponseEntity<ApiResponse> addStudent(@PathVariable String id, @RequestParam String studentId){
        try{
            Classroom classroom = classroomService.addStudent(id, studentId);
            return ResponseEntity.ok(new ApiResponse("Add student success", classroom));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Add student failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/{id}/add-students")
    public ResponseEntity<ApiResponse> addStudents(@PathVariable String id, @RequestBody List<String> studentIds){
        try{
            Classroom classroom = classroomService.addStudents(id, studentIds);
            return ResponseEntity.ok(new ApiResponse("Add students success", classroom));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Add students failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/{id}/delete-student")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable String id, @RequestParam String studentId){
        try{
            classroomService.deleteStudent(id, studentId);
            return ResponseEntity.ok(new ApiResponse("Delete student success", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Delete student failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/{id}/delete-class")
    public ResponseEntity<ApiResponse> deleteClass(@PathVariable String id){
        try{
            classroomService.deleteClass(id);
            return ResponseEntity.ok(new ApiResponse("Delete class success", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Delete class failed " + e.getMessage(), null));
        }
    }
}

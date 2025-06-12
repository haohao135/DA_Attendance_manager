package com.da.Attendance.controller;

import com.da.Attendance.dto.request.Classroom.AddClassroomRequest;
import com.da.Attendance.dto.request.Classroom.UpdateClassroomRequest;
import com.da.Attendance.dto.response.ApiResponse;
import com.da.Attendance.model.Classroom;
import com.da.Attendance.service.ClassroomService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/class")
@CrossOrigin("*")
public class ClassroomController {
    @Autowired
    private ClassroomService classroomService;
    @GetMapping("/get")
    public ResponseEntity<ApiResponse> getClass(@RequestParam String id){
        try{
            Classroom classroom = classroomService.findClassById(id);
            return ResponseEntity.ok(new ApiResponse("Get class success", classroom));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Get class failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/get/teacher")
    public ResponseEntity<ApiResponse> getByTeacher(@RequestParam String id){
        try{
            List<Classroom> classrooms = classroomService.findByTeacherId(id);
            return ResponseEntity.ok(new ApiResponse("Get class success", classrooms));
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
    public ResponseEntity<ApiResponse> addClass(@RequestBody AddClassroomRequest addClassroomRequest){
        try{
            Classroom classroom = classroomService.addClass(addClassroomRequest);
            return ResponseEntity.ok(new ApiResponse("Add class success", classroom));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Add class failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/add-teacher")
    public ResponseEntity<ApiResponse> addTeacher(@RequestParam String id, @RequestParam String teacherId){
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
    @PostMapping("/add-student")
    public ResponseEntity<ApiResponse> addStudent(@RequestParam String id, @RequestParam String studentId){
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
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> deleteClass(@RequestParam String id){
        try{
            classroomService.deleteClass(id);
            return ResponseEntity.ok(new ApiResponse("Delete class success", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Delete class failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllClass(){
        try{
            List<Classroom> classrooms = classroomService.getAllClass();
            return ResponseEntity.ok(new ApiResponse("Get all class success", classrooms));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Get all class failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateClass(@RequestParam String id, @RequestBody UpdateClassroomRequest update){
        try{
            classroomService.updateClass(id, update);
            return ResponseEntity.ok(new ApiResponse("Update class success", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update class failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/report")
    public ResponseEntity<byte[]> downloadAttendanceReport(@RequestParam String classId) {
        ByteArrayInputStream stream = classroomService.exportAttendanceReport(classId);
        if (stream == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        String filename = "attendance_report_" + classId + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(stream.readAllBytes());
    }
    @PostMapping("/delete-student")
    public ResponseEntity<ApiResponse> deleteStudent(
            @RequestParam String id,
            @RequestParam String studentId) {
        try {
            if (id == null || id.isBlank() || studentId == null || studentId.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("Class ID and Student ID must not be empty", null));
            }

            classroomService.deleteStudent(id, studentId);
            return ResponseEntity.ok(
                    new ApiResponse("Delete student from class success", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Delete student from class failed: " + e.getMessage(), null));
        }
    }
    @PostMapping("/import-students")
    public ResponseEntity<ApiResponse> importStudentByCsv(
            @RequestParam String id,
            @RequestParam("file") MultipartFile file) {
        try {
            classroomService.importStudentsFromCSV(id, file);
            return ResponseEntity.ok(
                    new ApiResponse("Import students success", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Import students failed: " + e.getMessage(), null));
        }
    }
}

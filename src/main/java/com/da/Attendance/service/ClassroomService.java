package com.da.Attendance.service;

import com.da.Attendance.dto.request.Classroom.AddClassroomRequest;
import com.da.Attendance.dto.request.Classroom.UpdateClassroomRequest;
import com.da.Attendance.dto.response.Report.ClassReport;
import com.da.Attendance.model.Classroom;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface ClassroomService {
    Classroom findClassById(String id);
    List<Classroom> findByStudentId(String studentId);
    List<Classroom> findByTeacherId(String teacherId);
    Classroom addClass(AddClassroomRequest addClassroomRequest);
    Classroom addTeacher(String id, String teacherId);
    Classroom updateClassName(String id, String className);
    Classroom updateTeacher(String id, String teacherId);
    Classroom addStudent(String id, String studentId);
    Classroom addStudents(String id, List<String> studentIds);
    void deleteStudent(String id, String studentId);
    void deleteClass(String id);
    List<Classroom> getAllClass();
    void updateClass(String id, UpdateClassroomRequest updateClassroomRequest);
    ByteArrayInputStream exportAttendanceReport(String id);
    void importStudentsFromCSV(String classroomId, MultipartFile file) throws IOException;
}

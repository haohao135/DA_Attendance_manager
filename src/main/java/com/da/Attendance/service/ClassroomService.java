package com.da.Attendance.service;

import com.da.Attendance.model.Classroom;

import java.util.List;

public interface ClassroomService {
    Classroom findClassById(String id);
    List<Classroom> findByStudentId(String studentId);
    Classroom addClass(String className, String classId);
    Classroom addTeacher(String id, String teacherId);
    Classroom updateClassName(String id, String className);
    Classroom updateTeacher(String id, String teacherId);
    Classroom addStudent(String id, String studentId);
    Classroom addStudents(String id, List<String> studentIds);
    void deleteStudent(String id, String studentId);
    void deleteClass(String id);
}

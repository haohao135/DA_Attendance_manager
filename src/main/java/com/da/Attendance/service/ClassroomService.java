package com.da.Attendance.service;

import com.da.Attendance.model.Classroom;

public interface ClassroomService {
    Classroom findClassById(String id);
    Classroom addClass(String className);
    Classroom addTeacher(String id, String teacherId);
    Classroom updateClassName(String id, String className);
    Classroom updateTeacher(String id, String teacherId);
    Classroom addStudent(String id, String studentId);
    void deleteStudent(String id, String studentId);
    void deleteClass(String id);
}

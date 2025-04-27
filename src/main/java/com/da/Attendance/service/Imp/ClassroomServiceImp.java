package com.da.Attendance.service.Imp;

import com.da.Attendance.dto.request.Classroom.AddClassroomRequest;
import com.da.Attendance.model.Classroom;
import com.da.Attendance.repository.ClassroomRepository;
import com.da.Attendance.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClassroomServiceImp implements ClassroomService {
    @Autowired
    private ClassroomRepository classroomRepository;

    @Override
    public Classroom findClassById(String id) {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("class not found"));
    }

    @Override
    public List<Classroom> findByStudentId(String studentId) {
        return classroomRepository.findByStudentIdsContaining(studentId);
    }

    @Override
    public List<Classroom> findByTeacherId(String teacherId) {
        return classroomRepository.findByTeacherId(teacherId);
    }

    @Override
    public Classroom addClass(AddClassroomRequest addClassroomRequest) {
        if (addClassroomRequest.getClassName() == null || addClassroomRequest.getClassName().isBlank()) {
            throw new IllegalArgumentException("class name cannot be null or empty");
        }
        Classroom classroom = new Classroom();
        classroom.setClassName(addClassroomRequest.getClassName());
        classroom.setClassId(addClassroomRequest.getClassId());
        classroom.setRoom(addClassroomRequest.getRoom());
        return classroomRepository.save(classroom);
    }

    @Override
    public Classroom addTeacher(String id, String teacherId) {
        if (teacherId == null || teacherId.isBlank()) {
            throw new IllegalArgumentException("teacher ID cannot be null or empty");
        }
        Classroom classroom = findClassById(id);
        classroom.setTeacherId(teacherId);
        return classroomRepository.save(classroom);
    }

    @Override
    public Classroom updateClassName(String id, String className) {
        if (className == null || className.isBlank()) {
            throw new IllegalArgumentException("class name cannot be null or empty");
        }
        Classroom classroom = findClassById(id);
        classroom.setClassName(className);
        return classroomRepository.save(classroom);
    }

    @Override
    public Classroom updateTeacher(String id, String teacherId) {
        if (teacherId == null || teacherId.isBlank()) {
            throw new IllegalArgumentException("teacher ID cannot be null or empty");
        }
        Classroom classroom = findClassById(id);
        classroom.setTeacherId(teacherId);
        return classroomRepository.save(classroom);
    }

    @Override
    @Transactional
    public Classroom addStudent(String id, String studentId) {
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("student ID cannot be null or empty");
        }
        Classroom classroom = findClassById(id);
        if (!classroom.getStudentIds().contains(studentId)) {
            classroom.getStudentIds().add(studentId);
            return classroomRepository.save(classroom);
        }
        throw new RuntimeException("student already exists in the class");
    }

    @Override
    public Classroom addStudents(String id, List<String> studentIds) {
        Classroom classroom = findClassById(id);
        List<String> currentStudents = classroom.getStudentIds();
        for (String studentId : studentIds) {
            if (!currentStudents.contains(studentId)) {
                currentStudents.add(studentId);
            }
        }
        classroom.setStudentIds(currentStudents);
        return classroomRepository.save(classroom);
    }

    @Override
    @Transactional
    public void deleteStudent(String id, String studentId) {
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("student ID cannot be null or empty");
        }
        Classroom classroom = findClassById(id);
        if (classroom.getStudentIds().remove(studentId)) {
            classroomRepository.save(classroom);
        } else {
            throw new RuntimeException("student not found in the class");
        }
    }

    @Override
    public void deleteClass(String id) {
        if (!classroomRepository.existsById(id)) {
            throw new RuntimeException("class not found");
        }
        classroomRepository.deleteById(id);
    }
}

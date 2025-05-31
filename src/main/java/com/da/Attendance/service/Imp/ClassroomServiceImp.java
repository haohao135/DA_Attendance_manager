package com.da.Attendance.service.Imp;

import com.da.Attendance.dto.request.Classroom.AddClassroomRequest;
import com.da.Attendance.dto.request.Classroom.UpdateClassroomRequest;
import com.da.Attendance.dto.response.Report.ClassReport;
import com.da.Attendance.model.AttendanceRecord;
import com.da.Attendance.model.AttendanceSession;
import com.da.Attendance.model.Classroom;
import com.da.Attendance.model.User;
import com.da.Attendance.model.enums.AttendanceStatus;
import com.da.Attendance.repository.AttendanceRecordRepository;
import com.da.Attendance.repository.AttendanceSessionRepository;
import com.da.Attendance.repository.ClassroomRepository;
import com.da.Attendance.repository.UserRepository;
import com.da.Attendance.service.ClassroomService;
import com.da.Attendance.service.UserService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassroomServiceImp implements ClassroomService {
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private AttendanceSessionRepository attendanceSessionRepository;
    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;
    @Autowired
    private UserRepository userRepository;

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
        classroom.setTeacherId(addClassroomRequest.getTeacherId());
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

    @Override
    public List<Classroom> getAllClass() {
        return classroomRepository.findAll();
    }

    @Override
    public void updateClass(String id, UpdateClassroomRequest updateClassroomRequest) {
        Classroom classroom = findClassById(id);
        classroom.setClassName(updateClassroomRequest.getClassName());
        classroom.setClassId(updateClassroomRequest.getClassId());
        classroom.setRoom(updateClassroomRequest.getRoom());
        classroom.setTeacherId(updateClassroomRequest.getTeacherId());
        classroomRepository.save(classroom);
    }

    @Override
    public ByteArrayInputStream exportAttendanceReport(String id) {
        Classroom classroom = classroomRepository.findById(id).orElseThrow();
        List<String> studentIds = classroom.getStudentIds().stream().distinct().toList();

        List<AttendanceSession> sessions = attendanceSessionRepository
                .findByClassIdOrderByDateTimeAsc(id);

        List<String> sessionIds = sessions.stream()
                .map(AttendanceSession::getId)
                .toList();

        List<AttendanceRecord> records = attendanceRecordRepository.findByAttendanceSessionIdIn(sessionIds);

        // Tạo map: studentId -> (sessionId -> status)
        Map<String, Map<String, AttendanceStatus>> attendanceMap = new HashMap<>();
        for (AttendanceRecord record : records) {
            attendanceMap
                    .computeIfAbsent(record.getStudentId(), k -> new HashMap<>())
                    .put(record.getAttendanceSessionId(), record.getStatus());
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Attendance Report");

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zoneId);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Hàng tiêu đề
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Student Name");
        for (int i = 0; i < sessions.size(); i++) {
            LocalDate sessionDate = sessions.get(i).getDateTime().atZone(zoneId).toLocalDate();
            header.createCell(i + 1).setCellValue(sessionDate.format(dateFormatter));
        }

        // Ghi từng dòng sinh viên
        int rowIndex = 1;
        for (String studentId : studentIds) {
            Optional<User> optionalUser = userRepository.findById(studentId);
            if (optionalUser.isEmpty()) continue;

            User user = optionalUser.get();
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(user.getFullName());

            for (int j = 0; j < sessions.size(); j++) {
                AttendanceSession session = sessions.get(j);
                String sessionId = session.getId();
                LocalDate sessionDate = session.getDateTime().atZone(zoneId).toLocalDate();

                if (sessionDate.isAfter(today)) {
                    row.createCell(j + 1).setCellValue("");
                    continue;
                }

                AttendanceStatus status = attendanceMap
                        .getOrDefault(studentId, Collections.emptyMap())
                        .get(sessionId);

                String statusStr = switch (status) {
                    case PRESENT -> "Có mặt";
                    case ABSENT -> "Vắng";
                    case LATE -> "Trễ";
                    default -> "";
                };

                row.createCell(j + 1).setCellValue(statusStr);
            }
        }

        // Tự động chỉnh kích thước cột
        for (int i = 0; i <= sessions.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi workbook ra output stream
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            workbook.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export attendance report", e);
        }
    }

}

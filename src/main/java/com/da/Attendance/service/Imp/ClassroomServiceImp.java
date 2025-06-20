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
import com.da.Attendance.service.AttendanceRecordService;
import com.da.Attendance.service.ClassroomService;
import com.da.Attendance.service.UserService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
    @Autowired
    private AttendanceRecordService attendanceRecordService;

    @Override
    public Classroom findClassById(String id) {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("class not found"));
    }

    @Override
    public List<Classroom> findByStudentId(String studentId) {
        return classroomRepository.findByStudentIdsContainingAndIsActiveTrue(studentId);
    }

    @Override
    public List<Classroom> findByTeacherId(String teacherId) {
        return classroomRepository.findByTeacherIdAndIsActiveTrue(teacherId);
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
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }

        Classroom classroom = findClassById(id);

        if (classroom.getStudentIds().contains(studentId)) {
            throw new RuntimeException("Student already exists in the class");
        }

        classroom.getStudentIds().add(studentId);
        Classroom savedClassroom = classroomRepository.save(classroom);

        List<AttendanceSession> sessions = attendanceSessionRepository.findByClassId(id);
        for (AttendanceSession session : sessions) {
            if (!session.getAttendanceRecordsStudentId().contains(studentId)) {
                session.getAttendanceRecordsStudentId().add(studentId);
                attendanceRecordService.addOne(session, studentId);
            }
        }
        attendanceSessionRepository.saveAll(sessions);

        return savedClassroom;
    }


    @Override
    @Transactional
    public Classroom addStudents(String id, List<String> studentIds) {
        if (studentIds == null || studentIds.isEmpty()) {
            throw new IllegalArgumentException("Student list cannot be null or empty");
        }

        Classroom classroom = findClassById(id);
        Set<String> updatedStudentSet = new HashSet<>(classroom.getStudentIds());
        boolean hasNewStudent = false;

        for (String studentId : studentIds) {
            if (studentId != null && !studentId.isBlank() && updatedStudentSet.add(studentId)) {
                hasNewStudent = true;
            }
        }

        if (hasNewStudent) {
            classroom.setStudentIds(new ArrayList<>(updatedStudentSet));
            classroomRepository.save(classroom);
            List<AttendanceSession> sessions = attendanceSessionRepository.findByClassId(id);

            for (AttendanceSession session : sessions) {
                Set<String> sessionStudentIds = new HashSet<>(session.getAttendanceRecordsStudentId());
                boolean sessionChanged = false;

                for (String studentId : studentIds) {
                    if (studentId != null && !studentId.isBlank() && sessionStudentIds.add(studentId)) {
                        attendanceRecordService.addOne(session, studentId);
                        sessionChanged = true;
                    }
                }
                if (sessionChanged) {
                    session.setAttendanceRecordsStudentId(new ArrayList<>(sessionStudentIds));
                }
            }
            attendanceSessionRepository.saveAll(sessions);
        }

        return classroom;
    }


    @Override
    @Transactional
    public void deleteStudent(String id, String studentId) {
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("student ID cannot be null or empty");
        }
        Classroom classroom = findClassById(id);

        boolean removedFromClass = classroom.getStudentIds().remove(studentId);
        if (!removedFromClass) {
            throw new RuntimeException("Student not found in the class");
        }
        classroomRepository.save(classroom);
        List<AttendanceSession> sessions = attendanceSessionRepository.findByClassId(id);

        for (AttendanceSession session : sessions) {
            List<String> studentIds = session.getAttendanceRecordsStudentId();
            if (studentIds.contains(studentId)) {
                studentIds.remove(studentId);
                session.setAttendanceRecordsStudentId(studentIds);
                attendanceRecordService.deleteOne(session, studentId);
            }
        }
        attendanceSessionRepository.saveAll(sessions);
    }


    @Override
    @Transactional
    public void deleteClass(String id) {
        if (!classroomRepository.existsById(id)) {
            throw new RuntimeException("Class not found");
        }
        List<AttendanceSession> sessions = attendanceSessionRepository.findByClassId(id);
        for (AttendanceSession session : sessions) {
            attendanceRecordService.deleteAllBySessionId(session.getId());
        }
        attendanceSessionRepository.deleteAll(sessions);
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
    public void importStudentsFromCSV(String classroomId, MultipartFile file) throws IOException {
        List<String> studentIds = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean skipHeader = true;

            while ((line = reader.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String email = line.trim();
                if (!email.isEmpty()) {
                    userRepository.findByEmail(email)
                            .ifPresentOrElse(
                                    user -> studentIds.add(user.getId()),
                                    () -> System.out.println("user not found with email: " + email)
                            );
                }
            }
        }
        if (!studentIds.isEmpty()) {
            addStudents(classroomId, studentIds);
        }
    }

    @Override
    public void updateIsActive(String id, boolean isActive) {
        Classroom classroom = findClassById(id);
        classroom.setActive(isActive);
        classroomRepository.save(classroom);
    }
}

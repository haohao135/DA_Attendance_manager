package com.da.Attendance.service.Imp;

import com.da.Attendance.dto.request.AttendanceRecord.AddAttendanceRecordRequest;
import com.da.Attendance.dto.request.AttendanceSession.AddAttendanceSessionRequest;
import com.da.Attendance.dto.request.AttendanceSession.UpdateAttendanceSessionRequest;
import com.da.Attendance.dto.response.AttendanceSession.AttendanceSessionScheduleResponse;
import com.da.Attendance.model.*;
import com.da.Attendance.model.enums.AttendanceStatus;
import com.da.Attendance.repository.AttendanceSessionRepository;
import com.da.Attendance.repository.ClassroomRepository;
import com.da.Attendance.service.AttendanceRecordService;
import com.da.Attendance.service.AttendanceSessionService;
import com.da.Attendance.service.ClassroomService;
import com.da.Attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceSessionServiceImp implements AttendanceSessionService {
    @Autowired
    AttendanceSessionRepository attendanceSessionRepository;
    @Autowired
    AttendanceRecordService attendanceRecordService;
    @Autowired
    ClassroomService classroomService;
    @Autowired
    UserService userService;
    @Autowired
    ClassroomRepository classroomRepository;
    @Override
    public List<AttendanceSession> getAttendanceSessionAbsences(String studentId) {
        List<AttendanceRecord> attendanceRecords = attendanceRecordService.getAbsences(studentId);
        List<AttendanceSession> attendanceSessions = new ArrayList<>();
        for (AttendanceRecord i : attendanceRecords){
            AttendanceSession a = getById(i.getAttendanceSessionId());
            attendanceSessions.add(a);
        }
        return attendanceSessions;
    }

    @Override
    public AttendanceSession getById(String id) {
        Optional<AttendanceSession> attendanceSession = attendanceSessionRepository.findById(id);
        if(attendanceSession.isEmpty()){
            throw new RuntimeException("attendanceSession not found");
        }
        return attendanceSession.get();
    }

    @Override
    public AttendanceSession add(AddAttendanceSessionRequest addAttendanceSessionRequest) {
        AttendanceSession attendanceSession = new AttendanceSession();
        attendanceSession.setClassId(addAttendanceSessionRequest.getClassId());
        attendanceSession.setClassName(addAttendanceSessionRequest.getClassName());
        attendanceSession.setDateTime(addAttendanceSessionRequest.getDateTime());
        attendanceSession.setMethod(addAttendanceSessionRequest.getMethod());
        attendanceSession.setAttendanceRecordsStudentId(addAttendanceSessionRequest.getAttendanceRecordsStudentId());
        AttendanceSession savedSession = attendanceSessionRepository.save(attendanceSession);

        AddAttendanceRecordRequest addAttendanceRecordRequest = new AddAttendanceRecordRequest();
        addAttendanceRecordRequest.setAttendanceSessionId(savedSession.getId());
        addAttendanceRecordRequest.setAttendanceRecordsStudentId(savedSession.getAttendanceRecordsStudentId());
        addAttendanceRecordRequest.setStatus(AttendanceStatus.ABSENT);
        addAttendanceRecordRequest.setMethod(savedSession.getMethod());
        addAttendanceRecordRequest.setTimestamp(savedSession.getDateTime());

        attendanceRecordService.add(addAttendanceRecordRequest);
        return savedSession;
    }

    @Override
    public List<AttendanceSessionScheduleResponse> getAttendanceSessionScheduleByUserId(String id) {
        List<AttendanceSession> attendanceSessions =
                attendanceSessionRepository.findByAttendanceRecordsStudentIdContains(id);
        List<AttendanceSessionScheduleResponse> attendanceSessionScheduleResponseList = new ArrayList<>();
        for(AttendanceSession a : attendanceSessions){
            Classroom classroom = classroomService.findClassById(a.getClassId());
            User user = userService.getUserById(classroom.getTeacherId());
            AttendanceSessionScheduleResponse attendanceSessionScheduleResponse =
                    new AttendanceSessionScheduleResponse(
                            a.getClassName(),
                            classroom.getClassId(),
                            user.getFullName(),
                            a.getDateTime(),
                            classroom.getRoom()
                    );
            attendanceSessionScheduleResponseList.add(attendanceSessionScheduleResponse);
        }
        return attendanceSessionScheduleResponseList;
    }

    @Override
    public List<AttendanceSessionScheduleResponse> getAttendanceSessionsByTeacherId(String teacherId) {
        List<Classroom> classrooms = classroomRepository.findByTeacherId(teacherId);
        List<String> classIds = classrooms.stream()
                .map(Classroom::getId)
                .collect(Collectors.toList());
        if (classIds.isEmpty()) {
            return List.of();
        }

        List<AttendanceSession> attendanceSessions = attendanceSessionRepository.findByClassIdIn(classIds);

        List<AttendanceSessionScheduleResponse> attendanceSessionScheduleResponseList = new ArrayList<>();
        for (AttendanceSession a : attendanceSessions) {
            Classroom classroom = classroomService.findClassById(a.getClassId());
            if (classroom == null) {
                continue;
            }

            User user = userService.getUserById(classroom.getTeacherId());
            if (user == null) {
                continue;
            }
            AttendanceSessionScheduleResponse attendanceSessionScheduleResponse =
                    new AttendanceSessionScheduleResponse(
                            a.getClassName(),
                            classroom.getClassId(),
                            user.getFullName(),
                            a.getDateTime(),
                            classroom.getRoom()
                    );
            attendanceSessionScheduleResponseList.add(attendanceSessionScheduleResponse);
        }
        return attendanceSessionScheduleResponseList;
    }

    @Override
    public List<AttendanceSession> getAttendanceSessionsByClassId(String classId) {
        return attendanceSessionRepository.findByClassId(classId);
    }

    @Override
    public AttendanceSession update(String id, UpdateAttendanceSessionRequest updateAttendanceSessionRequest) {
        AttendanceSession attendanceSession = getById(id);
        attendanceSession.setClassName(updateAttendanceSessionRequest.getClassName());
        attendanceSession.setClassId(updateAttendanceSessionRequest.getClassId());
        attendanceSession.setDateTime(updateAttendanceSessionRequest.getDateTime());
        attendanceSession.setMethod(updateAttendanceSessionRequest.getMethod());
        attendanceSession.setAttendanceRecordsStudentId(updateAttendanceSessionRequest.getAttendanceRecordsStudentId());
        attendanceSessionRepository.save(attendanceSession);
        return null;
    }

    @Override
    public void delete(String id) {
        attendanceSessionRepository.deleteById(id);
    }

    @Override
    public AttendanceSession addOneStudent(String id, String studentId) {
        AttendanceSession attendanceSession = getById(id);
        if (!attendanceSession.getAttendanceRecordsStudentId().contains(studentId)) {
            attendanceSession.getAttendanceRecordsStudentId().add(studentId);
            AttendanceSession saveAttendanceSession = attendanceSessionRepository.save(attendanceSession);
            Classroom classroom = classroomService.findClassById(attendanceSession.getClassId());
            classroom.getStudentIds().add(studentId);
            classroomRepository.save(classroom);
            attendanceRecordService.addOne(attendanceSession, studentId);

            return saveAttendanceSession;
        }
        return attendanceSession;
    }

}

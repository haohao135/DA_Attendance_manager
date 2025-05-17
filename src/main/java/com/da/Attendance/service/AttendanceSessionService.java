package com.da.Attendance.service;

import com.da.Attendance.dto.request.AttendanceSession.AddAttendanceSessionRequest;
import com.da.Attendance.dto.request.AttendanceSession.UpdateAttendanceSessionRequest;
import com.da.Attendance.dto.response.AttendanceSession.AttendanceSessionScheduleResponse;
import com.da.Attendance.model.AttendanceSession;
import com.da.Attendance.repository.AttendanceSessionRepository;

import java.util.List;

public interface AttendanceSessionService {
    List<AttendanceSession> getAttendanceSessionAbsences(String studentId);
    AttendanceSession getById(String id);
    AttendanceSession add(AddAttendanceSessionRequest addAttendanceSessionRequest);
    List<AttendanceSessionScheduleResponse> getAttendanceSessionScheduleByUserId(String id);
    List<AttendanceSessionScheduleResponse> getAttendanceSessionsByTeacherId(String teacherId);
    List<AttendanceSession> getAttendanceSessionsByClassId(String classId);
    AttendanceSession update(String id, UpdateAttendanceSessionRequest updateAttendanceSessionRequest);
    void delete(String id);
    AttendanceSession addOneStudent(String id, String studentId);
}

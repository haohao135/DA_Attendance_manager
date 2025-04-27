package com.da.Attendance.dto.response.AttendanceSession;

import java.time.Instant;

public class AttendanceSessionScheduleResponse {
    private String className;
    private String classId;
    private String teacher;
    private Instant time;
    private String room;

    public AttendanceSessionScheduleResponse() {
    }

    public AttendanceSessionScheduleResponse(String className, String classId, String teacher, Instant time, String room) {
        this.className = className;
        this.classId = classId;
        this.teacher = teacher;
        this.time = time;
        this.room = room;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}

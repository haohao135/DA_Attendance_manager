package com.da.Attendance.dto.request.Classroom;

public class UpdateClassroomRequest {
    private String className;
    private String classId;
    private String room;
    private String teacherId;

    public UpdateClassroomRequest() {
    }

    public UpdateClassroomRequest(String className, String classId, String room, String teacherId) {
        this.className = className;
        this.classId = classId;
        this.room = room;
        this.teacherId = teacherId;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
}

package com.da.Attendance.dto.request.Classroom;

public class AddClassroomRequest {
    private String className;
    private String classId;
    private String room;

    public AddClassroomRequest() {
    }

    public AddClassroomRequest(String className, String classId, String room) {
        this.className = className;
        this.classId = classId;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}

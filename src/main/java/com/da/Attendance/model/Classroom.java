package com.da.Attendance.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Classroom {
    @Id
    private String id;
    private String className;
    private String teacherId;
    private String classId;
    private String room;
    private boolean isActive = true;
    private List<String> studentIds = new ArrayList<>();
    public Classroom() {}

    public Classroom(String className, String classId) {
        this.className = className;
        this.classId = classId;
    }

    public Classroom(String id, String className, String teacherId, String classId, String room, boolean isActive, List<String> studentIds) {
        this.id = id;
        this.className = className;
        this.teacherId = teacherId;
        this.classId = classId;
        this.room = room;
        this.isActive = isActive;
        this.studentIds = studentIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

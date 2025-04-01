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
    private List<String> studentIds = new ArrayList<>();

    public Classroom() {}

    public Classroom(String className) {
        this.className = className;
    }

    public Classroom(String id, String className, String teacherId, List<String> studentIds) {
        this.id = id;
        this.className = className;
        this.teacherId = teacherId;
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
}

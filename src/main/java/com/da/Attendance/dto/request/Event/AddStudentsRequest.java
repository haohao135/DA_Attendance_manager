package com.da.Attendance.dto.request.Event;

import java.util.List;

public class AddStudentsRequest {
    private String id;
    private List<String> studentIds;

    public AddStudentsRequest() {
    }

    public AddStudentsRequest(String id, List<String> studentIds) {
        this.id = id;
        this.studentIds = studentIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }
}

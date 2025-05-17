package com.da.Attendance.dto.request.User;

import com.da.Attendance.model.enums.UserRole;

import java.util.List;

public class FilterUserRequest {
    private UserRole userRole;
    private List<String> studentId;

    public FilterUserRequest() {
    }

    public FilterUserRequest(UserRole userRole, List<String> studentId) {
        this.userRole = userRole;
        this.studentId = studentId;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public List<String> getStudentId() {
        return studentId;
    }

    public void setStudentId(List<String> studentId) {
        this.studentId = studentId;
    }
}

package com.da.Attendance.dto.response.User;

public class UserRegisterResponse {
    private String email;
    private String fullName;

    public UserRegisterResponse() {
    }

    public UserRegisterResponse(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}

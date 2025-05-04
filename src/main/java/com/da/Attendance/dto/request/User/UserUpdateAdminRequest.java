package com.da.Attendance.dto.request.User;

public class UserUpdateAdminRequest {
    private String email;
    private String fullName;
    private String numberPhone;

    public UserUpdateAdminRequest() {
    }

    public UserUpdateAdminRequest(String email, String fullName, String numberPhone) {
        this.email = email;
        this.fullName = fullName;
        this.numberPhone = numberPhone;
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

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }
}
